package com.example.tfg;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tfg.Helpers.PreferenciasHelper;
import com.example.tfg.Helpers.ToastHelper;
import com.example.tfg.Helpers.NotificationHelper;
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

        solicitarPermisoNotificaciones();

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
                    if (!PreferenciasHelper.obtenerNotificaciones(EditActivity.this)) {
                        solicitarPermisoNotificaciones();
                        return;
                    }

                    Registro creado = response.body();
                    NotificationHelper.Notification(EditActivity.this, "Registro creado",
                            "Registro creado correctamente para " + creado.getNombre());
                    irSituacion1(creado.getNombre(), creado.getEuros());
                } else {
                    String error = "";
                    try {
                        error = response.errorBody() != null ? response.errorBody().string() : "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Registro-ERROR", "Código: " + response.code() + ", Respuesta: " + error);
                    NotificationHelper.Notification(EditActivity.this, "Error en registro",
                            "Error al crear registro. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Registro> call, Throwable t) {
                Log.e("Registro-FALLO", "Fallo de red: " + t.getMessage(), t);
                ToastHelper.error(EditActivity.this, "Fallo de red: " + t.getMessage());
                NotificationHelper.Notification(EditActivity.this, "Error de red",
                        "Fallo de red al crear registro: " + t.getMessage());
            }
        });
    }


    private void actualizarRegistro(Registro registro) {
        apiService.updateRegistro(registro.getId(), registro).enqueue(new Callback<Registro>() {
            @Override
            public void onResponse(Call<Registro> call, Response<Registro> response) {
                if (response.isSuccessful()) {
                    if (!PreferenciasHelper.obtenerNotificaciones(EditActivity.this)) {
                        solicitarPermisoNotificaciones();
                        return;
                    }

                    NotificationHelper.Notification(EditActivity.this, "Registro actualizado",
                            "Registro actualizado correctamente");
                    irSituacion1(registro.getNombre(), registro.getEuros());
                } else {
                    NotificationHelper.Notification(EditActivity.this, "Error al actualizar",
                            "Error al actualizar el registro");
                }
            }

            @Override
            public void onFailure(Call<Registro> call, Throwable t) {
                ToastHelper.error(EditActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void solicitarPermisoNotificaciones() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                PreferenciasHelper.guardarNotificaciones(this, true);
            } else {
                mostrarDialogoExplicacionNotificaciones();
            }
        }
    }

    private void mostrarDialogoExplicacionNotificaciones() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle("Notificaciones")
                .setMessage("Para recibir notificaciones sobre los registros, necesitas habilitarlas en los ajustes")
                .setPositiveButton("Ir a Ajustes", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void irSituacion1(String nombre, double euros) {
        Intent intent = new Intent(this, Situacion1Activity.class);
        intent.putExtra("registro_id", registroExistente.getId());
        intent.putExtra("euros", registroExistente.getEuros());
        startActivity(intent);

        finish();
    }
}
