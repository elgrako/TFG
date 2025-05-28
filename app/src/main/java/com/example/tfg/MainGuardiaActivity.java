package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainGuardiaActivity extends AppCompatActivity {

    ListView listViewGuardias;
    ArrayList<Guardia> listaGuardias;
    ArrayAdapter<Guardia> adapter;
    NotificationHelper nh;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guardia);

        apiService = RetrofitClient.getInstance().getApi();
        listViewGuardias = findViewById(R.id.listaGuardias);
        listaGuardias = new ArrayList<>();
        nh = new NotificationHelper();

        registerForContextMenu(listViewGuardias);

        TextView verGuardias = findViewById(R.id.verGuardias);
        TextView verJudiciales = findViewById(R.id.verJudiciales);
        verGuardias.setTextColor(Color.DKGRAY);
        verJudiciales.setTextColor(Color.BLUE);

        verJudiciales.setOnClickListener(v -> {
            startActivity(new Intent(MainGuardiaActivity.this, MainActivity.class));
            finish();
        });

        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.header_guardia, listViewGuardias, false);
        listViewGuardias.addHeaderView(headerView);

        Button newButton = findViewById(R.id.newGuardiaButton);
        newButton.setOnClickListener(v -> {
            startActivity(new Intent(MainGuardiaActivity.this, GuardiaActivity.class));
        });

        cargarGuardias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarGuardias();
    }

    private void cargarGuardias() {
        apiService.getAllGuardias().enqueue(new Callback<List<Guardia>>() {
            @Override
            public void onResponse(Call<List<Guardia>> call, Response<List<Guardia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaGuardias.clear();
                    listaGuardias.addAll(response.body());

                    adapter = new GuardiaAdapter(MainGuardiaActivity.this, listaGuardias);
                    listViewGuardias.setAdapter(adapter);
                } else {
                    ToastHelper.error(MainGuardiaActivity.this, "Error al cargar guardias");
                }
            }

            @Override
            public void onFailure(Call<List<Guardia>> call, Throwable t) {
                ToastHelper.error(MainGuardiaActivity.this, "Error de red: " + t.getMessage());
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.guardia_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if (position == 0) return false;

        Guardia guardiaSeleccionada = listaGuardias.get(position - 1);

        if (item.getItemId() == R.id.situacion_guardia_context) {
            Intent intent = new Intent(this, SituacionGuardiaActivity.class);
            intent.putExtra("guardia_id", guardiaSeleccionada.getId());
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.delete_guardia_context) {
            eliminarGuardia(guardiaSeleccionada);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void eliminarGuardia(Guardia guardia) {
        apiService.deleteGuardia((long) guardia.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    nh.Notification(
                            MainGuardiaActivity.this,
                            "Guardia eliminada",
                            "Se eliminó la guardia de " + guardia.getNombreAsistido()
                    );
                    cargarGuardias();
                } else {
                    ToastHelper.error(MainGuardiaActivity.this, "Error al eliminar guardia");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ToastHelper.error(MainGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }
}
