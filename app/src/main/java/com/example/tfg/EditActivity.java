package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    EditText nameField, dniField, nExpedienteField, eurosField;
    Button okButton, cancelButton;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbh = new DatabaseHelper(this);

        nameField = findViewById(R.id.nameEditField);
        dniField = findViewById(R.id.dniEditField);
        nExpedienteField = findViewById(R.id.nExpedienteEditField);
        eurosField = findViewById(R.id.eurosEditField);
        okButton = findViewById(R.id.nextButton);
        cancelButton = findViewById(R.id.backButton);

        nombre = getIntent().getStringExtra("nombre");

        if (nombre != null) {
            nameField.setText(nombre);
            nameField.setEnabled(false);

            dniField.setText(getIntent().getStringExtra("dni"));
            nExpedienteField.setText(getIntent().getStringExtra("nExpediente"));
            eurosField.setText(getIntent().getStringExtra("euros"));
        }

        okButton.setOnClickListener(v -> {
            guardarDatos();
            Intent okIntent = new Intent(this, Situacion1Activity.class);
            okIntent.putExtra("nombre", nombre);
            startActivity(okIntent);
            finish();
        });

        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void guardarDatos() {
        String dni = dniField.getText().toString().trim();
        String nExpediente = nExpedienteField.getText().toString().trim();
        double euros = Double.parseDouble(eurosField.getText().toString().trim());

        if (nombre == null) {
            nombre = nameField.getText().toString().trim();
        }

        Datos datos = new Datos(nombre, dni, nExpediente, euros);
        dbh.insertarDatosEdit(datos);
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
    }
}
