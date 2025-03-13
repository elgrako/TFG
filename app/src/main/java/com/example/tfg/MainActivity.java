package com.example.tfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        cargarDatos();

        listViewDatos.setOnItemClickListener((adapterView, view, position, id) -> {
            Datos datos = listaDatos.get(position);
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra("nombre", datos.getNombre());
            startActivity(intent);
        });
    }

    private void cargarDatos() {
        listaDatos.clear();
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

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDatos);
        listViewDatos.setAdapter(adapter);
    }
}
