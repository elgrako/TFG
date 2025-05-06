package com.example.tfg;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Currency;

public class Situacion1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_situacion1);

        Button finishButton = findViewById(R.id.next1SitButton);
        EditText Coments1Sit = findViewById(R.id.Coments1Sit);
        EditText NTalon1Sit = findViewById(R.id.NTalon1Sit);
        Switch Pendiente1Sit = findViewById(R.id.switchPendiente1Sit);
        Switch Validado1Sit = findViewById(R.id.switchValidado1Sit);
        Switch Pagado1Sit = findViewById(R.id.switchPagado1Sit2);
        TextView euros1Sit = findViewById(R.id.euros1Sit);

        Intent recoverIntent = getIntent();
        double euros = recoverIntent.getDoubleExtra("euros", 0.0);
        String nombre = recoverIntent.getStringExtra("nombre");

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("EUR"));
        String textoEuros = format.format(euros);
        euros1Sit.setText(textoEuros);

        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor cursor = dbh.obtenerSituacion1(nombre);
        if (cursor != null && cursor.moveToFirst()) {
            int presentado = cursor.getInt(0);
            int validado = cursor.getInt(1);
            int pagado = cursor.getInt(2);
            int nTalon = cursor.getInt(3);
            String comentarios = cursor.getString(4);

            Pendiente1Sit.setChecked(presentado == 1);
            Validado1Sit.setChecked(validado == 1);
            Pagado1Sit.setChecked(pagado == 1);
            NTalon1Sit.setText(String.valueOf(nTalon));
            Coments1Sit.setText(comentarios);

            cursor.close();
        }

        Pendiente1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) {
                Pendiente1Sit.setText("Aprobado");
                Pendiente1Sit.setTextColor(Color.GREEN);
            } else {
                Pendiente1Sit.setText("Pendiente");
                Pendiente1Sit.setTextColor(Color.RED);
            }
        });

        Validado1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) {
                Validado1Sit.setText("Validado");
                Validado1Sit.setTextColor(Color.GREEN);
            } else {
                Validado1Sit.setText("Por Validar");
                Validado1Sit.setTextColor(Color.RED);
            }
        });

        Pagado1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            if (isChecked) {
                Pagado1Sit.setText("Pagado");
                Pagado1Sit.setTextColor(Color.GREEN);
            } else {
                Pagado1Sit.setText("Por Pagar");
                Pagado1Sit.setTextColor(Color.RED);
            }
        });

        finishButton.setOnClickListener(v -> {
            int presentado = 0;
            if (Pendiente1Sit.isChecked()) {
                presentado = 1;
            }
            int validado = 0;
            if (Validado1Sit.isChecked()) {
                validado = 1;
            }
            int pagado = 0;
            if (Pagado1Sit.isChecked()) {
                pagado = 1;
            }

            String nTalonTexto = NTalon1Sit.getText().toString();
            int nTalon = 0;
            if (!nTalonTexto.isEmpty()) {
                try {
                    nTalon = Integer.parseInt(nTalonTexto);
                } catch (NumberFormatException e) {
                    nTalon = 0;
                }
            }

            String comentarios = Coments1Sit.getText().toString();

            boolean updated = dbh.updateSituacion1(nombre, presentado, validado, pagado, nTalon, comentarios);

            if (updated) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar/guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
