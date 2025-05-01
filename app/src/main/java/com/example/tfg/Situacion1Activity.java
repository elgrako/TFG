package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

        Button mainButton = findViewById(R.id.next1SitButton);
        EditText Coments1Sit = findViewById(R.id.Coments1Sit);
        EditText NTalon1Sit = findViewById(R.id.NTalon1Sit);
        Switch Pendiente1Sit = findViewById(R.id.switchPendiente1Sit);
        Switch Validado1Sit = findViewById(R.id.switchValidado1Sit);
        Switch Pagado1Sit = findViewById(R.id.switchPagado1Sit2);
        TextView euros1Sit = findViewById(R.id.euros1Sit);

        Intent recoverIntent = getIntent();
        double euros = recoverIntent.getDoubleExtra("euros", 0.0);

        mainButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

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

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("EUR"));
        String textoEuros = format.format(euros);
        euros1Sit.setText(textoEuros);


    }
}