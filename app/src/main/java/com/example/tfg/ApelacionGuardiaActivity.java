package com.example.tfg;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApelacionGuardiaActivity extends AppCompatActivity {

    Switch switchAdmitido, switchPresentado, switchSentencia;
    EditText expedienteField;
    Button guardarButton, cancelarButton;
    int guardiaId;
    Long apelacionId = null;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apelacion_guardia);

        switchAdmitido = findViewById(R.id.switchAdmitidoApelacion);
        switchPresentado = findViewById(R.id.switchPresentadoApelacion);
        switchSentencia = findViewById(R.id.switchSentenciaApelacion);
        expedienteField = findViewById(R.id.nExpedienteApelacionField);
        guardarButton = findViewById(R.id.guardarApelacionGuardiaButton);
        cancelarButton = findViewById(R.id.cancelarApelacionGuardiaButton);

        apiService = RetrofitClient.getInstance().getApi();

        guardiaId = getIntent().getIntExtra("guardia_id", -1);
        if (guardiaId == -1) {
            ToastHelper.error(this, "Guardia no válida");
            finish();
            return;
        }

        actualizarEstadoSwitch(switchAdmitido, false, "Admitido", "Rechazado");
        actualizarEstadoSwitch(switchPresentado, false, "Presentado", "Pendiente");
        actualizarEstadoSwitch(switchSentencia, false, "Sentencia", "Apelación");

        cargarApelacion();

        switchAdmitido.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchAdmitido, checked, "Admitido", "Rechazado"));
        switchPresentado.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchPresentado, checked, "Presentado", "Pendiente"));
        switchSentencia.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchSentencia, checked, "Sentencia", "Apelación"));

        guardarButton.setOnClickListener(v -> guardarApelacion());

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void cargarApelacion() {
        apiService.getApelacionByGuardiaId((long) guardiaId).enqueue(new Callback<ApelacionGuardia>() {
            @Override
            public void onResponse(Call<ApelacionGuardia> call, Response<ApelacionGuardia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApelacionGuardia apelacion = response.body();
                    apelacionId = apelacion.getId();

                    expedienteField.setText(apelacion.getnExpediente());
                    actualizarEstadoSwitch(switchAdmitido, apelacion.isAdmitido(), "Admitido", "Rechazado");
                    actualizarEstadoSwitch(switchPresentado, apelacion.isPresentado(), "Presentado", "Pendiente");
                    actualizarEstadoSwitch(switchSentencia, apelacion.isSentencia(), "Sentencia", "Apelación");
                }
            }

            @Override
            public void onFailure(Call<ApelacionGuardia> call, Throwable t) {
                ToastHelper.error(ApelacionGuardiaActivity.this, "Error al cargar datos: " + t.getMessage());
            }
        });
    }

    private void guardarApelacion() {
        String expediente = expedienteField.getText().toString().trim();
        boolean admitido = switchAdmitido.isChecked();
        boolean presentado = switchPresentado.isChecked();
        boolean sentencia = switchSentencia.isChecked();

        if (expediente.isEmpty()) {
            ToastHelper.error(this, "Introduce el número de expediente");
            return;
        }

        ApelacionGuardia apelacion = new ApelacionGuardia();
        apelacion.setGuardiaId((long) guardiaId);
        apelacion.setnExpediente(expediente);
        apelacion.setAdmitido(admitido);
        apelacion.setPresentado(presentado);
        apelacion.setSentencia(sentencia);

        Call<ApelacionGuardia> call;
        if (apelacionId != null) {
            apelacion.setId(apelacionId);
            call = apiService.updateApelacion(apelacionId, apelacion);
        } else {
            call = apiService.createApelacion(apelacion);
        }

        call.enqueue(new Callback<ApelacionGuardia>() {
            @Override
            public void onResponse(Call<ApelacionGuardia> call, Response<ApelacionGuardia> response) {
                if (response.isSuccessful()) {
                    NotificationHelper.Notification(
                            ApelacionGuardiaActivity.this,
                            "Apelación guardada",
                            "Expediente: " + expediente + " guardado correctamente."
                    );
                    finish();
                } else {
                    ToastHelper.error(ApelacionGuardiaActivity.this, "Error al guardar apelación");
                }
            }

            @Override
            public void onFailure(Call<ApelacionGuardia> call, Throwable t) {
                ToastHelper.error(ApelacionGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String textoOn, String textoOff) {
        s.setChecked(check);
        s.setText(check ? textoOn : textoOff);
        s.setTextColor(check ? Color.parseColor("#4CAF50") : Color.RED);
    }
}
