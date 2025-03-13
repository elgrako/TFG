package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    EditText nameField, dniField, nExpedienteField, eurosField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbh = new DatabaseHelper(this);

        nameField = findViewById(R.id.nameEditField);
        dniField = findViewById(R.id.dniEditField);
        nExpedienteField = findViewById(R.id.nExpedienteEditField);
        eurosField = findViewById(R.id.eurosEditField);

        String nombre = getIntent().getStringExtra("nombre");
        if (nombre != null) {
            nameField.setText(nombre);
            dniField.setText(getIntent().getStringExtra("dni"));
            nExpedienteField.setText(getIntent().getStringExtra("nExpediente"));
            eurosField.setText(getIntent().getStringExtra("euros"));
        }

        Button okButton = findViewById(R.id.nextButton);
        okButton.setOnClickListener(v -> {
            Intent okIntent = new Intent(this, Situacion1Activity.class);
            startActivity(okIntent);
            guardarDatos();
            finish();
        });
    }

    private void guardarDatos() {
        String nombre = nameField.getText().toString().trim();
        String dni = dniField.getText().toString().trim();
        String nExpediente = nExpedienteField.getText().toString().trim();
        double euros = Double.parseDouble(eurosField.getText().toString().trim());

        Datos datos = new Datos(nombre, dni, nExpediente, euros);
        dbh.insertarDatosEdit(datos);
    }
}