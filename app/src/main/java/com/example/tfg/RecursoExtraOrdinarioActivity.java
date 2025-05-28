package com.example.tfg;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.api.RetrofitClient;
import com.example.tfg.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecursoExtraOrdinarioActivity extends AppCompatActivity {

    EditText expedienteField;
    Switch switchAdmitido;
    Button guardarButton, cancelarButton;

    ApiService apiService;
    Long guardiaId;
    RecursoExtraOrdinario recursoExistente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_extra_ordinario);

        expedienteField = findViewById(R.id.nExpedienteRecursoExtraField);
        switchAdmitido = findViewById(R.id.switchAdmitidoRecursoExtra);
        guardarButton = findViewById(R.id.guardarRecursoExtraButton);
        cancelarButton = findViewById(R.id.cancelarRecursoExtraButton);

        apiService = RetrofitClient.getInstance().getApi();
        guardiaId = getIntent().getLongExtra("guardia_id", -1);

        if (guardiaId == -1) {
            ToastHelper.info(this, "Guardia no encontrada");
            finish();
            return;
        }

        cargarDatosSiExisten();

        switchAdmitido.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchAdmitido, checked, "Admitido", "Rechazado"));

        guardarButton.setOnClickListener(v -> guardarDatos());

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void cargarDatosSiExisten() {
        apiService.getRecursoExtraByGuardiaId(guardiaId).enqueue(new Callback<RecursoExtraOrdinario>() {
            @Override
            public void onResponse(Call<RecursoExtraOrdinario> call, Response<RecursoExtraOrdinario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recursoExistente = response.body();
                    expedienteField.setText(String.valueOf(recursoExistente.getnExpediente()));
                    actualizarEstadoSwitch(switchAdmitido, recursoExistente.getAdmitido(), "Admitido", "Rechazado");
                } else {
                    recursoExistente = null;
                    actualizarEstadoSwitch(switchAdmitido, false, "Admitido", "Rechazado");
                }
            }

            @Override
            public void onFailure(Call<RecursoExtraOrdinario> call, Throwable t) {
                ToastHelper.error(RecursoExtraOrdinarioActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void guardarDatos() {
        String expedienteStr = expedienteField.getText().toString().trim();

        if (expedienteStr.isEmpty()) {
            ToastHelper.info(this, "Introduce el número de expediente");
            return;
        }

        int nExpediente = Integer.parseInt(expedienteStr);
        boolean admitido = switchAdmitido.isChecked();

        if (recursoExistente != null) {
            recursoExistente.setnExpediente(nExpediente);
            recursoExistente.setAdmitido(admitido);

            apiService.updateRecursoExtra(recursoExistente.getId(), recursoExistente).enqueue(callback());
        } else {
            RecursoExtraOrdinario nuevo = new RecursoExtraOrdinario();
            nuevo.setGuardiaId(guardiaId);
            nuevo.setnExpediente(nExpediente);
            nuevo.setAdmitido(admitido);

            apiService.createRecursoExtra(nuevo).enqueue(callback());
        }
    }

    private Callback<RecursoExtraOrdinario> callback() {
        return new Callback<RecursoExtraOrdinario>() {
            @Override
            public void onResponse(Call<RecursoExtraOrdinario> call, Response<RecursoExtraOrdinario> response) {
                if (response.isSuccessful()) {
                    ToastHelper.info(RecursoExtraOrdinarioActivity.this, "Guardado correctamente");
                    finish();
                } else {
                    ToastHelper.error(RecursoExtraOrdinarioActivity.this, "Error al guardar");
                }
            }

            @Override
            public void onFailure(Call<RecursoExtraOrdinario> call, Throwable t) {
                ToastHelper.error(RecursoExtraOrdinarioActivity.this, "Fallo de red: " + t.getMessage());
            }
        };
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String textoOn, String textoOff) {
        s.setChecked(check);
        s.setText(check ? textoOn : textoOff);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
