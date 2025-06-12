package com.example.tfg.ui;

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
import com.example.tfg.R;
import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;
import com.example.tfg.local.entity.SituacionGuardia;

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
            Log.d("IntentDebug", "Extras recibidos: " + getIntent().getExtras());
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

                    cargarCamposEnPantalla();
                } else {
                    crearSituacionInicial();
                }
            }

            @Override
            public void onFailure(Call<SituacionGuardia> call, Throwable t) {
                ToastHelper.error(SituacionGuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void crearSituacionInicial() {
        situacionGuardia = new SituacionGuardia();
        situacionGuardia.setGuardiaId(guardiaId);
        situacionGuardia.setComentarios("");
        situacionGuardia.setNTalon("");
        situacionGuardia.setEuros("");
        situacionGuardia.setPresentado(false);
        situacionGuardia.setValidado(false);
        situacionGuardia.setPagado(false);

        apiService.createSituacionGuardia(situacionGuardia).enqueue(new Callback<SituacionGuardia>() {
            @Override
            public void onResponse(Call<SituacionGuardia> call, Response<SituacionGuardia> response) {
                if (response.isSuccessful()) {
                    situacionGuardia = response.body();
                    cargarCamposEnPantalla();
                } else {
                    String errorMsg = "";
                    try {
                        errorMsg = response.errorBody() != null ? response.errorBody().string() : "Sin mensaje";
                    } catch (Exception e) {
                        errorMsg = "Error parsing cuerpo de error";
                    }

                    Log.e("SituacionGuardia", "Código: " + response.code() + " | Error: " + errorMsg);
                    ToastHelper.error(SituacionGuardiaActivity.this,
                            "Error al inicializar situación\nCódigo: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SituacionGuardia> call, Throwable t) {
                ToastHelper.error(SituacionGuardiaActivity.this, "Fallo al inicializar situación: " + t.getMessage());
            }
        });
    }

    private void cargarCamposEnPantalla() {
        comentariosField.setText(situacionGuardia.getComentarios());
        nTalonField.setText(situacionGuardia.getNTalon());
        eurosField.setText(situacionGuardia.getEuros());
        actualizarEstadoSwitch(presentadoSwitch, situacionGuardia.getPresentado(), "Presentado", "Pendiente");
        actualizarEstadoSwitch(validadoSwitch, situacionGuardia.getValidado(), "Validado", "Por Validar");
        actualizarEstadoSwitch(pagadoSwitch, situacionGuardia.getPagado(), "Pagado", "Por Pagar");
        Log.d("LOAD_DEBUG", "Cargando situación:");
        Log.d("LOAD_DEBUG", "Comentarios: " + situacionGuardia.getComentarios());
        Log.d("LOAD_DEBUG", "nTalon: " + situacionGuardia.getNTalon());
        Log.d("LOAD_DEBUG", "Euros: " + situacionGuardia.getEuros());

    }


    private void guardarCambios() {
        if (situacionGuardia == null || guardiaId == null) {
            ToastHelper.error(this, "Situación no disponible");
            return;
        }

        situacionGuardia.setComentarios(comentariosField.getText().toString().trim());
        situacionGuardia.setNTalon(nTalonField.getText().toString().trim());
        situacionGuardia.setEuros(eurosField.getText().toString().trim());
        situacionGuardia.setPresentado(presentadoSwitch.isChecked());
        situacionGuardia.setValidado(validadoSwitch.isChecked());
        situacionGuardia.setPagado(pagadoSwitch.isChecked());

        Log.d("SAVE_DEBUG", "Guardando Situación:");
        Log.d("SAVE_DEBUG", "Comentarios: " + situacionGuardia.getComentarios());
        Log.d("SAVE_DEBUG", "nTalon: " + situacionGuardia.getNTalon());
        Log.d("SAVE_DEBUG", "Euros: " + situacionGuardia.getEuros());

        apiService.updateSituacionGuardia(situacionGuardia.getId(), situacionGuardia).enqueue(new Callback<SituacionGuardia>() {
            @Override
            public void onResponse(Call<SituacionGuardia> call, Response<SituacionGuardia> response) {
                if (response.isSuccessful()) {
                    nh.Notification(SituacionGuardiaActivity.this, "Situación guardada", "Se actualizó correctamente");
                    finish();
                } else {
                    Log.e("SituacionGuardia", "Error codigo: " + response.code());
                    ToastHelper.error(SituacionGuardiaActivity.this, "Error al guardar. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SituacionGuardia> call, Throwable t) {
                Log.e("SituacionGuardia", "Fallo red: " + t.getMessage());
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
