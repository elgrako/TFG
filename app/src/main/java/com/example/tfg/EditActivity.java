package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.Helpers.ToastHelper;
import com.example.tfg.api.RetrofitClient;
import com.example.tfg.api.ApiService;
import com.example.tfg.entities.Registro;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    EditText nameField, dniField, nExpedienteField, eurosField, emailField, telefonoField;
    Button okButton, cancelButton;
    Registro registroExistente;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        apiService = RetrofitClient.getInstance().getApi();

        nameField = findViewById(R.id.nameEditField);
        dniField = findViewById(R.id.dniEditField);
        nExpedienteField = findViewById(R.id.nExpedienteEditField);
        eurosField = findViewById(R.id.eurosEditField);
        emailField = findViewById(R.id.emailEditField);
        telefonoField = findViewById(R.id.telefonoEditField);

        okButton = findViewById(R.id.nextEditButton);
        cancelButton = findViewById(R.id.backEditButton);

        registroExistente = (Registro) getIntent().getSerializableExtra("registro");

        if (registroExistente != null) {
            nameField.setText(registroExistente.getNombre());
            dniField.setText(registroExistente.getDni());
            nExpedienteField.setText(registroExistente.getnExpediente());
            emailField.setText(registroExistente.getEmail());
            eurosField.setText(String.valueOf(registroExistente.getEuros()));
            telefonoField.setText(String.valueOf(registroExistente.getTelefono()));
        }

        okButton.setOnClickListener(v -> guardarRegistro());

        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void guardarRegistro() {
        String nombre = nameField.getText().toString().trim();
        String dni = dniField.getText().toString().trim();
        String nExpediente = nExpedienteField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String eurosStr = eurosField.getText().toString().trim();
        String telefonoStr = telefonoField.getText().toString().trim();

        if (nombre.isEmpty() || dni.isEmpty() || nExpediente.isEmpty() || eurosStr.isEmpty() || telefonoStr.isEmpty()) {
            ToastHelper.error(this, "Completa todos los campos");
            return;
        }

        double euros;
        int telefono;

        try {
            euros = Double.parseDouble(eurosStr);
            telefono = Integer.parseInt(telefonoStr);
        } catch (NumberFormatException e) {
            ToastHelper.error(this, "Formato incorrecto en números");
            return;
        }

        if (registroExistente != null) {
            Registro registro = new Registro(
                    registroExistente.getId(),
                    nombre, dni, nExpediente, euros, email, telefono
            );
            actualizarRegistro(registro);
        } else {
            Registro nuevoRegistro = new Registro();
            nuevoRegistro.setNombre(nombre);
            nuevoRegistro.setDni(dni);
            nuevoRegistro.setnExpediente(nExpediente);
            nuevoRegistro.setEuros(euros);
            nuevoRegistro.setEmail(email);
            nuevoRegistro.setTelefono(telefono);

            crearRegistro(nuevoRegistro);
        }
    }

    private void crearRegistro(Registro registro) {
        apiService.createRegistro(registro).enqueue(new Callback<Registro>() {
            @Override
            public void onResponse(Call<Registro> call, Response<Registro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ToastHelper.info(EditActivity.this, "Registro creado");
                    Registro creado = response.body();
                    irSituacion1(creado.getNombre(), creado.getEuros());
                } else {
                    String error = "";
                    try {
                        error = response.errorBody() != null ? response.errorBody().string() : "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Registro-ERROR", "Código: " + response.code() + ", Respuesta: " + error);
                    ToastHelper.error(EditActivity.this, "Error al crear registro. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Registro> call, Throwable t) {
                Log.e("Registro-FALLO", "Fallo de red: " + t.getMessage(), t);
                ToastHelper.error(EditActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }


    private void actualizarRegistro(Registro registro) {
        apiService.updateRegistro(registro.getId(), registro).enqueue(new Callback<Registro>() {
            @Override
            public void onResponse(Call<Registro> call, Response<Registro> response) {
                if (response.isSuccessful()) {
                    ToastHelper.info(EditActivity.this, "Registro actualizado");
                    irSituacion1(registro.getNombre(), registro.getEuros());
                } else {
                    ToastHelper.error(EditActivity.this, "Error al actualizar registro");
                }
            }

            @Override
            public void onFailure(Call<Registro> call, Throwable t) {
                ToastHelper.error(EditActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void irSituacion1(String nombre, double euros) {
        Intent intent = new Intent(this, Situacion1Activity.class);
        intent.putExtra("registro_id", registroExistente.getId());
        intent.putExtra("euros", registroExistente.getEuros());
        startActivity(intent);

        finish();
    }
}
