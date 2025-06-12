package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.Helpers.NotificationHelper;
import com.example.tfg.Helpers.PreferenciasHelper;
import com.example.tfg.Helpers.ToastHelper;
import com.example.tfg.api.RetrofitClient;
import com.example.tfg.entities.Registro;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView listViewDatos;
    ArrayList<Registro> listaDatos;
    RegistroAdapter adapter;
    Registro registroSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisoNotificaciones();

        View headerView = getLayoutInflater().inflate(R.layout.header_datos, null);

        listViewDatos = findViewById(R.id.listaDatosMain);
        listaDatos = new ArrayList<>();

        listViewDatos.addHeaderView(headerView);

        TextView verGuardias = findViewById(R.id.verGuardiasMain);
        TextView verJudiciales = findViewById(R.id.verJudicialesMain);

        verGuardias.setOnClickListener(v -> {
            verGuardias.setTextColor(Color.DKGRAY);
            verJudiciales.setTextColor(Color.BLUE);
            startActivity(new Intent(MainActivity.this, MainGuardiaActivity.class));
        });

        verJudiciales.setOnClickListener(v -> {
            verGuardias.setTextColor(Color.BLUE);
            verJudiciales.setTextColor(Color.DKGRAY);
        });

        Button newButton = findViewById(R.id.newJudicialButton);
        registerForContextMenu(listViewDatos);

        newButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        });

        listViewDatos.setOnItemClickListener((parent, view, position, id) -> {
            registroSeleccionado = listaDatos.get(position - 1);
        });

        cargarDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        RetrofitClient.getInstance().getApi().getAllRegistros()
                .enqueue(new Callback<List<Registro>>() {
                    @Override
                    public void onResponse(Call<List<Registro>> call, Response<List<Registro>> response) {
                        if (response.isSuccessful()) {
                            listaDatos.clear();
                            listaDatos.addAll(response.body());

                            if (adapter == null) {
                                adapter = new RegistroAdapter(MainActivity.this, listaDatos);
                                listViewDatos.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastHelper.error(MainActivity.this, "Error al obtener datos");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Registro>> call, Throwable th) {
                        ToastHelper.error(MainActivity.this, "Fallo de red: " + th.getMessage());
                    }
                });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info.position >= 1 && info.position <= listaDatos.size()) {
            registroSeleccionado = listaDatos.get(info.position - 1);
        } else {
            ToastHelper.error(this, "Indice inválido");
            return false;
        }

        int id = item.getItemId();
        Registro registro = registroSeleccionado;

        if (id == R.id.edit_context) {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("registro", registro);
            startActivity(intent);
            return true;
        } else if (id == R.id.correo_context) {
            String email = registro.getEmail();
            if (email != null && !email.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre expediente");
                startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            } else {
                ToastHelper.info(this, "No hay correo asignado");
            }
            return true;
        } else if (id == R.id.telefono_context) {
            int telefono = registro.getTelefono();
            if (telefono != 0) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + telefono));
                startActivity(callIntent);
            } else {
                ToastHelper.error(this, "No hay número de teléfono asignado");
            }
            return true;
        } else if (id == R.id.delete_context) {
            if (!PreferenciasHelper.obtenerNotificaciones(MainActivity.this)) {
                solicitarPermisoNotificaciones();
                return true;
            }

            mostrarDialogoConfirmacion(registro.getId());
            return true;
        }

        return super.onContextItemSelected(item);
    }


    private void solicitarPermisoNotificaciones() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PreferenciasHelper.guardarNotificaciones(this, true);
            } else {
                mostrarDialogoExplicacionNotificaciones();
            }
        }
    }

    private void mostrarDialogoExplicacionNotificaciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Notificaciones")
                .setMessage("Para recibir notificaciones sobre los registros, necesitas habilitarlas en los ajustes")
                .setPositiveButton("Ir a Ajustes", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void mostrarDialogoConfirmacion(Long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Eliminar registro")
                .setMessage("¿Estás seguro de eliminar este registro?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarRegistro(id))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void eliminarRegistro(Long id) {
        RetrofitClient.getInstance().getApi().deleteRegistro(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    NotificationHelper.Notification(MainActivity.this, "Registro eliminado",
                            "El registro ha sido eliminado correctamente");
                    cargarDatos();
                } else {
                    NotificationHelper.Notification(MainActivity.this, "Error al eliminar",
                            "Error al eliminar el registro");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                NotificationHelper.Notification(MainActivity.this, "Error de red",
                        "Fallo de red al eliminar el registro: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            PreferenciasHelper.cerrarSesion(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_manual) {
            startActivity(new Intent(this, ManualActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menu_multimedia) {
            startActivity(new Intent(this, MultimediaActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menu_websocket) {
            Intent intent = new Intent(MainActivity.this, WebSocketActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

