package com.example.tfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
        listViewDatos = findViewById(R.id.listaDatos);
        listaDatos = new ArrayList<>();

        Button newButton = findViewById(R.id.newButton);
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
        Cursor cursor = dbh.obtenerDatos();
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Datos datosSeleccionado = listaDatos.get(info.position);

        if (item.getItemId() == R.id.edit_context) {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("nombre", datosSeleccionado.getNombre());
            intent.putExtra("dni", datosSeleccionado.getDni());
            intent.putExtra("nExpediente", datosSeleccionado.getnExpediente());
            intent.putExtra("euros", datosSeleccionado.getEuros());
            startActivity(intent);
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
