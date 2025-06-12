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
import com.example.tfg.entities.ApelacionGuardia;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApelacionGuardiaActivity extends AppCompatActivity {

    Switch switchAdmitido, switchPresentado, switchSentencia;
    EditText expedienteField;
    Button guardarButton, cancelarButton, recursoButton;
    Long guardiaId;
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
        recursoButton = findViewById(R.id.irRecursoGuardiaButton);

        apiService = RetrofitClient.getInstance().getApi();

        guardiaId = getIntent().getLongExtra("guardia_id", -1);
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

        recursoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecursoGuardiaActivity.class);
            intent.putExtra("guardia_id", guardiaId);
            startActivity(intent);
        });
    }

    private void cargarApelacion() {
        apiService.getApelacionByGuardiaId(guardiaId).enqueue(new Callback<List<ApelacionGuardia>>() {
            @Override
            public void onResponse(Call<List<ApelacionGuardia>> call, Response<List<ApelacionGuardia>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ApelacionGuardia apelacion = response.body().get(0);
                    apelacionId = apelacion.getId();

                    expedienteField.setText(apelacion.getnExpediente());
                    actualizarEstadoSwitch(switchAdmitido, apelacion.isAdmitido(), "Admitido", "Rechazado");
                    actualizarEstadoSwitch(switchPresentado, apelacion.isPresentado(), "Presentado", "Pendiente");
                    actualizarEstadoSwitch(switchSentencia, apelacion.isSentencia(), "Sentencia", "Apelación");
                    Log.d("LOAD_DEBUG", "Apelación cargada:");
                    Log.d("LOAD_DEBUG", "ID: " + apelacion.getId());
                    Log.d("LOAD_DEBUG", "Expediente: " + apelacion.getnExpediente());
                    Log.d("LOAD_DEBUG", "Admitido: " + apelacion.isAdmitido());
                    Log.d("LOAD_DEBUG", "Presentado: " + apelacion.isPresentado());
                    Log.d("LOAD_DEBUG", "Sentencia: " + apelacion.isSentencia());

                } else {
                    ToastHelper.info(ApelacionGuardiaActivity.this, "No hay apelación registrada aún");
                }
            }

            @Override
            public void onFailure(Call<List<ApelacionGuardia>> call, Throwable t) {
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
        Log.d("SAVE_DEBUG", "Guardando apelación:");
        Log.d("SAVE_DEBUG", "Expediente: " + expediente);


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
                    String errorMsg = "";
                    try {
                        errorMsg = response.errorBody() != null ? response.errorBody().string() : "Sin mensaje";
                    } catch (Exception e) {
                        errorMsg = "Error leyendo el cuerpo de error";
                    }

                    Log.e("ApelacionGuardia", "Error al guardar: Código " + response.code() + " | Detalle: " + errorMsg);
                    ToastHelper.error(ApelacionGuardiaActivity.this,
                            "Error al guardar apelación\nCódigo: " + response.code() + "\n" + errorMsg);

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
