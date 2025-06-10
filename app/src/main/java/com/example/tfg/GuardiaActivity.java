package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import com.example.tfg.api.RetrofitClient;
import com.example.tfg.api.ApiService;
import com.example.tfg.Guardia;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuardiaActivity extends AppCompatActivity {

    TextView diaField;
    EditText nombreAsistidoField;
    Switch porJuzgadoSwitch, cobradoSwitch;
    Button guardarButton;
    NotificationHelper nh;
    private ApiService apiService;

    private Date selectedDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia);

        apiService = RetrofitClient.getInstance().getApi();
        nh = new NotificationHelper();

        diaField = findViewById(R.id.diaActuacionField);
        nombreAsistidoField = findViewById(R.id.nombreAsistidoField);
        porJuzgadoSwitch = findViewById(R.id.switchporJuzgado);
        cobradoSwitch = findViewById(R.id.switchcobrado);
        guardarButton = findViewById(R.id.guardarGuardiaButton);

        selectedDate = new Date();
        diaField.setText(sdf.format(selectedDate));

        diaField.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);

            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        selectedDate = calendar.getTime();
                        diaField.setText(sdf.format(selectedDate));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        actualizarEstadoSwitch(porJuzgadoSwitch, false, "Sí por pasa Juzgado", "No por por Juzgado");
        actualizarEstadoSwitch(cobradoSwitch, false, "Cobrado", "No cobrado");

        porJuzgadoSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                actualizarEstadoSwitch(porJuzgadoSwitch, isChecked, "Sí por pasa Juzgado", "No por por Juzgado"));

        cobradoSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                actualizarEstadoSwitch(cobradoSwitch, isChecked, "Cobrado", "No cobrado"));

        guardarButton.setOnClickListener(v -> guardarGuardia());
    }

    private void guardarGuardia() {
        String nombre = nombreAsistidoField.getText().toString().trim();
        boolean juzgado = porJuzgadoSwitch.isChecked();
        boolean cobrado = cobradoSwitch.isChecked();

        if (nombre.isEmpty()) {
            ToastHelper.info(this, "Introduce el nombre del asistido");
            return;
        }

        String diaActuacion = sdf.format(selectedDate);
        Guardia guardia = new Guardia();
        guardia.setNombreAsistido(nombre);
        guardia.setDiaActuacion(diaActuacion);
        guardia.setPorJuzgado(juzgado);
        guardia.setCobrado(cobrado);

        apiService.createGuardia(guardia).enqueue(new Callback<Guardia>() {
            @Override
            public void onResponse(Call<Guardia> call, Response<Guardia> response) {
                if (response.isSuccessful()) {
                    nh.Notification(GuardiaActivity.this, "Guardia registrada",
                            "Guardia guardada correctamente para " + nombre);
                    Intent intent = new Intent(GuardiaActivity.this, SituacionGuardiaActivity.class);
                    intent.putExtra("nombreAsistido", nombre);
                    startActivity(intent);
                    finish();
                } else {
                    String error = "";
                    try {
                        error = response.errorBody() != null ? response.errorBody().string() : "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Guardia-ERROR", "Código: " + response.code() + ", Respuesta: " + error);
                    ToastHelper.error(GuardiaActivity.this, "Error al guardar guardia. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Guardia> call, Throwable t) {
                Log.e("Guardia-FALLO", "Fallo de red: " + t.getMessage(), t);
                ToastHelper.error(GuardiaActivity.this, "Fallo de red: " + t.getMessage());
            }
        });
    }


    private void actualizarEstadoSwitch(Switch s, boolean check, String TextYes, String TextNo) {
        s.setChecked(check);
        if (check) {
            s.setText(TextYes);
            s.setTextColor(Color.GREEN);
        } else {
            s.setText(TextNo);
            s.setTextColor(Color.RED);
        }
    }
}