package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.Helpers.NotificationHelper;
import com.example.tfg.Helpers.ToastHelper;
import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;
import com.example.tfg.entities.RecursoGuardia;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecursoGuardiaActivity extends AppCompatActivity {

    EditText expedienteRecursoField;
    Switch switchResuelto;
    Button guardarButton, cancelarButton, btnIrRecursoExtra;

    ApiService apiService;
    Long guardiaId;
    RecursoGuardia recursoExistente;
    NotificationHelper nh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_guardia);

        nh = new NotificationHelper();

        expedienteRecursoField = findViewById(R.id.nExpedienteRecursoField);
        switchResuelto = findViewById(R.id.switchResueltoRecurso);
        guardarButton = findViewById(R.id.guardarRecursoGuardiaButton);
        cancelarButton = findViewById(R.id.cancelarRecursoGuardiaButton);
        btnIrRecursoExtra = findViewById(R.id.btnIrRecursoExtra);

        apiService = RetrofitClient.getInstance().getApi();
        guardiaId = getIntent().getLongExtra("guardia_id", -1);

        if (guardiaId == -1) {
            ToastHelper.error(this, "Guardia no valida");
            finish();
            return;
        }

        cargarDatosSiExisten();

        switchResuelto.setOnCheckedChangeListener((btn, check) ->
                actualizarEstadoSwitch(switchResuelto, check, "Resolucion", "Pendiente"));

        guardarButton.setOnClickListener(v -> guardarDatos());

        cancelarButton.setOnClickListener(v -> finish());

        btnIrRecursoExtra.setOnClickListener(v -> {
            Log.d("INTENT_DEBUG", "Saliendo hacia RecursoExtraOrdinario con guardiaId: " + guardiaId);
            Intent intent = new Intent(this, RecursoExtraOrdinarioActivity.class);
            intent.putExtra("guardia_id", guardiaId);
            startActivity(intent);
        });
    }

    private void cargarDatosSiExisten() {
        apiService.getRecursoGuardiaByGuardiaId(guardiaId).enqueue(new Callback<RecursoGuardia>() {
            @Override
            public void onResponse(Call<RecursoGuardia> call, Response<RecursoGuardia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recursoExistente = response.body();
                    expedienteRecursoField.setText(recursoExistente.getnExpediente());
                    actualizarEstadoSwitch(switchResuelto, recursoExistente.getResuelto(), "Resolución", "Pendiente");
                } else {
                    recursoExistente = null;
                    actualizarEstadoSwitch(switchResuelto, false, "Resolución", "Pendiente");
                }
            }

            @Override
            public void onFailure(Call<RecursoGuardia> call, Throwable t) {
                ToastHelper.error(RecursoGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    // En RecursoGuardiaActivity.java, actualiza el método guardarDatos:
    private void guardarDatos() {
        String expediente = expedienteRecursoField.getText().toString().trim();
        boolean resuelto = switchResuelto.isChecked();

        if (expediente.isEmpty()) {
            ToastHelper.info(this, "Introduce el número de expediente");
            return;
        }

        // Validate guardiaId
        if (guardiaId == null) {
            Log.e("RecursoGuardia", "GuardiaId no válido: " + guardiaId);
            ToastHelper.error(this, "Error: Guardia no válida");
            return;
        }

        RecursoGuardia recurso;
        if (recursoExistente != null) {
            recurso = recursoExistente;
            recurso.setnExpediente(expediente);
            recurso.setResuelto(resuelto);

            Log.d("RecursoGuardia", "Actualizando recurso: " +
                    "id: " + recurso.getId() +
                    ", expediente: " + recurso.getnExpediente() +
                    ", resuelto: " + recurso.getResuelto());

            apiService.updateRecursoGuardia(recurso.getId(), recurso).enqueue(new Callback<RecursoGuardia>() {
                @Override
                public void onResponse(Call<RecursoGuardia> call, Response<RecursoGuardia> response) {
                    if (response.isSuccessful()) {
                        nh.Notification(RecursoGuardiaActivity.this, "Recurso actualizado", "Se actualizó correctamente");
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sin detalles";
                            Log.e("RecursoGuardia", "Error al actualizar: " + response.code() +
                                    ", Mensaje: " + errorBody);
                        } catch (Exception e) {
                            Log.e("RecursoGuardia", "Error al leer respuesta: " + e.getMessage());
                        }
                        ToastHelper.error(RecursoGuardiaActivity.this, "Error al actualizar recurso: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RecursoGuardia> call, Throwable t) {
                    Log.e("RecursoGuardia", "Fallo de red: " + t.getMessage(), t);
                    ToastHelper.error(RecursoGuardiaActivity.this, "Fallo de red al actualizar: " + t.getMessage());
                }
            });
        } else {
            recurso = new RecursoGuardia();
            recurso.setGuardiaId(guardiaId);
            recurso.setnExpediente(expediente);
            recurso.setResuelto(resuelto);

            Log.d("RecursoGuardia", "Creando nuevo recurso: " +
                    "guardiaId: " + recurso.getGuardiaId() +
                    ", expediente: " + recurso.getnExpediente() +
                    ", resuelto: " + recurso.getResuelto());

            apiService.createRecursoGuardia(recurso).enqueue(new Callback<RecursoGuardia>() {
                @Override
                public void onResponse(Call<RecursoGuardia> call, Response<RecursoGuardia> response) {
                    if (response.isSuccessful()) {
                        nh.Notification(RecursoGuardiaActivity.this, "Recurso creado", "Se creó correctamente");
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sin detalles";
                            Log.e("RecursoGuardia", "Error al crear: " + response.code() +
                                    ", Mensaje: " + errorBody);
                        } catch (Exception e) {
                            Log.e("RecursoGuardia", "Error al leer respuesta: " + e.getMessage());
                        }
                        ToastHelper.error(RecursoGuardiaActivity.this, "Error al crear recurso: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RecursoGuardia> call, Throwable t) {
                    Log.e("RecursoGuardia", "Fallo de red: " + t.getMessage(), t);
                    ToastHelper.error(RecursoGuardiaActivity.this, "Fallo de red al crear: " + t.getMessage());
                }
            });
        }
    }


    private void actualizarEstadoSwitch(Switch s, boolean check, String on, String off) {
        s.setChecked(check);
        s.setText(check ? on : off);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
