package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    DatabaseHelper dbh;
    EditText nameField, dniField, nExpedienteField, eurosField, emailField, telefonoField;
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
        emailField = findViewById(R.id.emailEditField);
        telefonoField = findViewById(R.id.telefonoEditField);

        okButton = findViewById(R.id.nextEditButton);
        cancelButton = findViewById(R.id.backEditButton);

        nombre = getIntent().getStringExtra("nombre");

        if (nombre != null) {
            nameField.setText(nombre);

            dniField.setText(getIntent().getStringExtra("dni"));
            nExpedienteField.setText(getIntent().getStringExtra("nExpediente"));
            emailField.setText(getIntent().getStringExtra("email"));

            if (getIntent().hasExtra("euros")) {
                double euros = getIntent().getDoubleExtra("euros", 0.0);
                eurosField.setText(String.valueOf(euros));
            }
            if (getIntent().hasExtra("telefono")) {
                int telefono = getIntent().getIntExtra("telefono", 0);
                telefonoField.setText(String.valueOf(telefono));
            }
        }

        okButton.setOnClickListener(v -> {
            guardarDatos();
            double euros = Double.parseDouble(eurosField.getText().toString().trim());

            Intent okIntent = new Intent(this, Situacion1Activity.class);
            okIntent.putExtra("nombre", nombre);
            okIntent.putExtra("euros", euros);
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
        String email = emailField.getText().toString().trim();
        int telefono = Integer.parseInt(telefonoField.getText().toString().trim());

        if (nombre == null) {
            nombre = nameField.getText().toString().trim();
        }

        Datos datos = new Datos(nombre, dni, nExpediente, euros, email, telefono);
        dbh.insertarOActualizarDatosEdit(datos);
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show();
    }
}
