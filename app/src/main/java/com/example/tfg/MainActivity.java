package com.example.tfg;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    ListView listViewDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView listViewDatos = findViewById(R.id.listaDatos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }

    public void cargarDatos() {
        ArrayList<Datos> listaDatos = new ArrayList<>();
        Cursor cursor = dbh.obtenerDatos();
        while (cursor.moveToNext()){
            @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            @SuppressLint("Range") String dni = cursor.getString(cursor.getColumnIndex("dni"));
            @SuppressLint("Range") String nExpediente = cursor.getString(cursor.getColumnIndex("nExpediente"));
            @SuppressLint("Range") double euros = cursor.getDouble(cursor.getColumnIndex("euros"));
            listaDatos.add(new Datos(nombre, dni,  nExpediente, euros));
        }
        cursor.close();

        ArrayAdapter<Datos> adapterlista = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listaDatos);
        listViewDatos.setAdapter(adapterlista);
    }
}