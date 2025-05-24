package com.example.tfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    ListView listViewDatos;
    ArrayList<Datos> listaDatos;
    ArrayAdapter<Datos> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbh = new DatabaseHelper(this);
        listViewDatos = findViewById(R.id.listaDatosMain);
        listaDatos = new ArrayList<>();

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

        LayoutInflater inflater = getLayoutInflater();
        View headerView = inflater.inflate(R.layout.header_datos, listViewDatos, false);
        listViewDatos.addHeaderView(headerView);

        Button newButton = findViewById(R.id.newJudicialButton);
        registerForContextMenu(listViewDatos);

        cargarDatos();

        newButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    private void cargarDatos() {
        listaDatos.clear();
        Cursor cursor = dbh.obtenerDatosMain();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") String dni = cursor.getString(cursor.getColumnIndex("dni"));
                @SuppressLint("Range") String nExpediente = cursor.getString(cursor.getColumnIndex("nExpediente"));
                @SuppressLint("Range") double euros = cursor.getDouble(cursor.getColumnIndex("euros"));
                listaDatos.add(new Datos(nombre, dni, nExpediente, euros));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new DatosAdapter(this, listaDatos);
        listViewDatos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context, menu);
    }


    @SuppressLint("Range")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Datos datosSeleccionado = listaDatos.get(info.position - 1);

        Cursor cursor = dbh.getExtrasByNombre(datosSeleccionado.getNombre());

        String email = "";
        int telefono = 0;

        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex("email"));
            telefono = cursor.getInt(cursor.getColumnIndex("telefono"));
            cursor.close();
        }

        int id = item.getItemId();

        if (id == R.id.edit_context) {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("nombre", datosSeleccionado.getNombre());
            intent.putExtra("dni", datosSeleccionado.getDni());
            intent.putExtra("nExpediente", datosSeleccionado.getnExpediente());
            intent.putExtra("euros", datosSeleccionado.getEuros());
            intent.putExtra("email", email);
            intent.putExtra("telefono", telefono);
            startActivity(intent);
            return true;

        } else if (id == R.id.correo_context) {
            if (email != null && !email.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre el expediente");
                startActivity(Intent.createChooser(emailIntent, "Enviar email"));
            } else {
                Toast.makeText(this, "No hay un correo electronico asignado", Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (id == R.id.telefono_context) {
            if (telefono != 0) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + telefono));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "No hay un numero de teléfono asignado", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (item.getItemId() == R.id.delete_context) {
            boolean deleted = dbh.borrarJudicialPorNombre(datosSeleccionado.getNombre());
            if (deleted) {
                Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                cargarDatos();
            } else {
                Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        return super.onContextItemSelected(item);
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
}
