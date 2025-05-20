package com.example.tfg;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainGuardiaActivity extends AppCompatActivity {

    DatabaseHelper dbh;
    ListView listViewGuardias;
    ArrayList<Guardia> listaGuardias;
    ArrayAdapter<Guardia> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guardia);

        dbh = new DatabaseHelper(this);
        listViewGuardias = findViewById(R.id.listaGuardias);
        listaGuardias = new ArrayList<>();

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
            Intent intent = new Intent(MainGuardiaActivity.this, GuardiaActivity.class);
            startActivity(intent);
        });

        cargarGuardias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarGuardias();
    }

    private void cargarGuardias() {
        listaGuardias.clear();
        @SuppressLint("Range") Cursor cursor = dbh.obtenerGuardias();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombreAsistido"));
                @SuppressLint("Range") String dia = cursor.getString(cursor.getColumnIndex("diaActuacion"));
                @SuppressLint("Range") int juzgado = cursor.getInt(cursor.getColumnIndex("porJuzgado"));
                @SuppressLint("Range") int cobrado = cursor.getInt(cursor.getColumnIndex("cobrado"));

                listaGuardias.add(new Guardia(nombre, dia, juzgado == 1, cobrado == 1));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new GuardiaAdapter(this, listaGuardias);
        listViewGuardias.setAdapter(adapter);
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
