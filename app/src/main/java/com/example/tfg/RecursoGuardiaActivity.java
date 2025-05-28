package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_guardia);

        expedienteRecursoField = findViewById(R.id.nExpedienteRecursoField);
        switchResuelto = findViewById(R.id.switchResueltoRecurso);
        guardarButton = findViewById(R.id.guardarRecursoGuardiaButton);
        cancelarButton = findViewById(R.id.cancelarRecursoGuardiaButton);
        btnIrRecursoExtra = findViewById(R.id.btnIrRecursoExtra);

        apiService = RetrofitClient.getInstance().getApi();
        guardiaId = getIntent().getLongExtra("guardia_id", -1);

        if (guardiaId == -1) {
            ToastHelper.error(this, "Guardia no válida");
            finish();
            return;
        }

        cargarDatosSiExisten();

        switchResuelto.setOnCheckedChangeListener((btn, check) ->
                actualizarEstadoSwitch(switchResuelto, check, "Resolución", "Pendiente"));

        guardarButton.setOnClickListener(v -> guardarDatos());

        cancelarButton.setOnClickListener(v -> finish());

        btnIrRecursoExtra.setOnClickListener(v -> {
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

    private void guardarDatos() {
        String expediente = expedienteRecursoField.getText().toString().trim();
        boolean resuelto = switchResuelto.isChecked();

        if (expediente.isEmpty()) {
            ToastHelper.info(this, "Introduce el número de expediente");
            return;
        }

        if (recursoExistente != null) {
            recursoExistente.setnExpediente(expediente);
            recursoExistente.setResuelto(resuelto);
            apiService.updateRecursoGuardia(recursoExistente.getId(), recursoExistente).enqueue(getCallback());
        } else {
            RecursoGuardia nuevo = new RecursoGuardia();
            nuevo.setGuardiaId(guardiaId);
            nuevo.setnExpediente(expediente);
            nuevo.setResuelto(resuelto);
            apiService.createRecursoGuardia(nuevo).enqueue(getCallback());
        }
    }

    private Callback<RecursoGuardia> getCallback() {
        return new Callback<RecursoGuardia>() {
            @Override
            public void onResponse(Call<RecursoGuardia> call, Response<RecursoGuardia> response) {
                if (response.isSuccessful()) {
                    ToastHelper.info(RecursoGuardiaActivity.this, "Guardado correctamente");
                    finish();
                } else {
                    ToastHelper.error(RecursoGuardiaActivity.this, "Error al guardar");
                }
            }

            @Override
            public void onFailure(Call<RecursoGuardia> call, Throwable t) {
                ToastHelper.error(RecursoGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        };
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String on, String off) {
        s.setChecked(check);
        s.setText(check ? on : off);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
