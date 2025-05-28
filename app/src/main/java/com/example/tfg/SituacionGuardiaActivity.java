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

public class SituacionGuardiaActivity extends AppCompatActivity {

    EditText comentariosField, nTalonField, eurosField;
    Switch presentadoSwitch, validadoSwitch, pagadoSwitch;
    Button cancelarButton, guardarButton, btnIrApelacion;
    NotificationHelper nh;
    Long guardiaId;
    ApiService apiService;
    SituacionGuardia situacionGuardia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacion_guardia);

        apiService = RetrofitClient.getInstance().getApi();
        nh = new NotificationHelper();

        comentariosField = findViewById(R.id.comentariosGuardiaField);
        nTalonField = findViewById(R.id.nTalonGuardiaField);
        eurosField = findViewById(R.id.eurosGuardiaField);
        presentadoSwitch = findViewById(R.id.switchPresentadoGuardia);
        validadoSwitch = findViewById(R.id.switchValidadoGuardia);
        pagadoSwitch = findViewById(R.id.switchPagadoGuardia);
        cancelarButton = findViewById(R.id.cancelarSituacionGuardiaButton);
        guardarButton = findViewById(R.id.guardarSituacionGuardiaButton);
        btnIrApelacion = findViewById(R.id.btnIrApelacion);

        guardiaId = getIntent().getLongExtra("guardia_id", -1);

        if (guardiaId == -1) {
            ToastHelper.error(this, "Error al recibir la guardia");
            finish();
            return;
        }

        cargarSituacionGuardia();

        btnIrApelacion.setOnClickListener(v -> {
            Intent intent = new Intent(this, ApelacionGuardiaActivity.class);
            intent.putExtra("guardia_id", guardiaId);
            startActivity(intent);
        });

        presentadoSwitch.setOnCheckedChangeListener((b, isChecked) ->
                actualizarEstadoSwitch(presentadoSwitch, isChecked, "Presentado", "Pendiente"));

        validadoSwitch.setOnCheckedChangeListener((b, isChecked) ->
                actualizarEstadoSwitch(validadoSwitch, isChecked, "Validado", "Por Validar"));

        pagadoSwitch.setOnCheckedChangeListener((b, isChecked) ->
                actualizarEstadoSwitch(pagadoSwitch, isChecked, "Pagado", "Por Pagar"));

        guardarButton.setOnClickListener(v -> guardarCambios());

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void cargarSituacionGuardia() {
        apiService.getByGuardiaId(guardiaId).enqueue(new Callback<SituacionGuardia>() {
            @Override
            public void onResponse(Call<SituacionGuardia> call, Response<SituacionGuardia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    situacionGuardia = response.body();
                    comentariosField.setText(situacionGuardia.getComentarios());
                    nTalonField.setText(situacionGuardia.getNTalon());
                    eurosField.setText(situacionGuardia.getEuros());
                    actualizarEstadoSwitch(presentadoSwitch, situacionGuardia.getPresentado(), "Presentado", "Pendiente");
                    actualizarEstadoSwitch(validadoSwitch, situacionGuardia.getValidado(), "Validado", "Por Validar");
                    actualizarEstadoSwitch(pagadoSwitch, situacionGuardia.getPagado(), "Pagado", "Por Pagar");
                } else {
                    ToastHelper.error(SituacionGuardiaActivity.this, "No se pudo cargar la situación");
                }
            }

            @Override
            public void onFailure(Call<SituacionGuardia> call, Throwable t) {
                ToastHelper.error(SituacionGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void guardarCambios() {
        if (situacionGuardia == null) {
            ToastHelper.error(this, "Situación no disponible");
            return;
        }

        situacionGuardia.setComentarios(comentariosField.getText().toString().trim());
        situacionGuardia.setNTalon(nTalonField.getText().toString().trim());
        situacionGuardia.setEuros(eurosField.getText().toString().trim());
        situacionGuardia.setPresentado(presentadoSwitch.isChecked());
        situacionGuardia.setValidado(validadoSwitch.isChecked());
        situacionGuardia.setPagado(pagadoSwitch.isChecked());

        apiService.updateSituacionGuardia(guardiaId, situacionGuardia).enqueue(new Callback<SituacionGuardia>() {
            @Override
            public void onResponse(Call<SituacionGuardia> call, Response<SituacionGuardia> response) {
                if (response.isSuccessful()) {
                    nh.Notification(SituacionGuardiaActivity.this, "Situación guardada", "Se actualizó la situación de la guardia");
                    finish();
                } else {
                    ToastHelper.error(SituacionGuardiaActivity.this, "Error al guardar la situación");
                }
            }

            @Override
            public void onFailure(Call<SituacionGuardia> call, Throwable t) {
                ToastHelper.error(SituacionGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String textoOn, String textoOff) {
        s.setChecked(check);
        s.setText(check ? textoOn : textoOff);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
