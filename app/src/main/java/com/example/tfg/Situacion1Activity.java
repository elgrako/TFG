package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Situacion1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_situacion1);

        Button mainButton = findViewById(R.id.next1SitButton);
        EditText Coments1Sit = findViewById(R.id.Coments1Sit);
        EditText NTalon1Sit = findViewById(R.id.NTalon1Sit);
        TextView Pendiente1Sit = findViewById(R.id.switchPendiente1Sit);
        TextView Validado1Sit = findViewById(R.id.switchValidado1Sit);
        TextView Pagado1Sit = findViewById(R.id.switchPagado1Sit2);
        TextView euros1Sit = findViewById(R.id.euros1Sit);

        mainButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        Pendiente1Sit.setText("Aprobado");
        Pendiente1Sit.setTextColor(Color.GREEN);

    }
}