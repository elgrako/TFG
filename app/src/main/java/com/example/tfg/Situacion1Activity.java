package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.api.ApiService;
import com.example.tfg.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.NumberFormat;
import java.util.Currency;

public class Situacion1Activity extends AppCompatActivity {
    NotificationHelper nh;
    ApiService apiService;

    EditText Coments1Sit, NTalon1Sit;
    Switch Pendiente1Sit, Validado1Sit, Pagado1Sit;
    TextView euros1Sit;

    Registro registro;
    Long registroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_situacion1);

        nh = new NotificationHelper();
        apiService = RetrofitClient.getInstance().getApi();

        Button finishButton = findViewById(R.id.next1SitButton);
        Button cancelButton = findViewById(R.id.cancel1SitButton);
        Coments1Sit = findViewById(R.id.Coments1Sit);
        NTalon1Sit = findViewById(R.id.NTalon1Sit);
        Pendiente1Sit = findViewById(R.id.switchPendiente1Sit);
        Validado1Sit = findViewById(R.id.switchValidado1Sit);
        Pagado1Sit = findViewById(R.id.switchPagado1Sit2);
        euros1Sit = findViewById(R.id.euros1Sit);

        registroId = getIntent().getLongExtra("registro_id", -1);
        double euros = getIntent().getDoubleExtra("euros", 0.0);

        if (registroId == -1) {
            ToastHelper.error(this, "ID de registro no válido");
            finish();
            return;
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("EUR"));
        euros1Sit.setText(format.format(euros));

        cargarDatos();

        Pendiente1Sit.setOnCheckedChangeListener((b, isChecked) -> actualizarSwitch(Pendiente1Sit, isChecked, "Aprobado", "Pendiente"));
        Validado1Sit.setOnCheckedChangeListener((b, isChecked) -> actualizarSwitch(Validado1Sit, isChecked, "Validado", "Por Validar"));
        Pagado1Sit.setOnCheckedChangeListener((b, isChecked) -> actualizarSwitch(Pagado1Sit, isChecked, "Pagado", "Por Pagar"));

        finishButton.setOnClickListener(v -> guardarCambios());

        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void cargarDatos() {
        apiService.getRegistroById(registroId).enqueue(new Callback<Registro>() {
            @Override
            public void onResponse(Call<Registro> call, Response<Registro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registro = response.body();
                    actualizarSwitch(Pendiente1Sit, registro.getPresentado(), "Aprobado", "Pendiente");
                    actualizarSwitch(Validado1Sit, registro.getValidado(), "Validado", "Por Validar");
                    actualizarSwitch(Pagado1Sit, registro.getPagado(), "Pagado", "Por Pagar");
                    NTalon1Sit.setText(String.valueOf(registro.getnTalon() != null ? registro.getnTalon() : ""));
                    Coments1Sit.setText(registro.getComentarios() != null ? registro.getComentarios() : "");
                } else {
                    ToastHelper.error(Situacion1Activity.this, "No se pudo cargar la situación");
                }
            }

            @Override
            public void onFailure(Call<Registro> call, Throwable t) {
                ToastHelper.error(Situacion1Activity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }

    private void guardarCambios() {
        if (registro == null || registro.getId() == null) {
            ToastHelper.error(this, "Registro no disponible para guardar");
            return;
        }

        registro.setPresentado(Pendiente1Sit.isChecked());
        registro.setValidado(Validado1Sit.isChecked());
        registro.setPagado(Pagado1Sit.isChecked());

        String nTalonTexto = NTalon1Sit.getText().toString().trim();
        Integer nTalon = nTalonTexto.isEmpty() ? null : Integer.parseInt(nTalonTexto);
        String comentarios = Coments1Sit.getText().toString().trim();

        apiService.updateSituacion1(
                registro.getId(),
                registro.getPresentado(),
                registro.getValidado(),
                registro.getPagado(),
                nTalon,
                comentarios
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    nh.Notification(Situacion1Activity.this, "Situación actualizada", "Cambios guardados");
                    startActivity(new Intent(Situacion1Activity.this, MainActivity.class));
                    finish();
                } else {
                    Log.e("Situacion1", "Error código: " + response.code());
                    ToastHelper.error(Situacion1Activity.this, "Error al guardar (código " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Situacion1", "Fallo red: " + t.getMessage());
                ToastHelper.error(Situacion1Activity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }


    private void actualizarSwitch(Switch s, boolean checked, String yes, String no) {
        s.setChecked(checked);
        s.setText(checked ? yes : no);
        s.setTextColor(checked ? Color.GREEN : Color.RED);
    }
}
