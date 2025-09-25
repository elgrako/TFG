package com.example.tfg.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import com.example.tfg.Helpers.NotificationHelper;
import com.example.tfg.Helpers.ToastHelper;
import com.example.tfg.R;
import com.example.tfg.api.RetrofitClient;
import com.example.tfg.api.ApiService;
import com.example.tfg.local.entity.Guardia;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuardiaActivity extends AppCompatActivity {

    EditText diaActuacionField;
    EditText nombreAsistidoField;
    Switch porJuzgadoSwitch, cobradoSwitch;
    Button guardarButton;
    NotificationHelper nh;
    private ApiService apiService;

    private Calendar selectedDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia);

        apiService = RetrofitClient.getInstance().getApi();
        nh = new NotificationHelper();

        diaActuacionField = findViewById(R.id.diaActuacionField);
        nombreAsistidoField = findViewById(R.id.nombreAsistidoField);
        porJuzgadoSwitch = findViewById(R.id.switchporJuzgado);
        cobradoSwitch = findViewById(R.id.switchcobrado);
        guardarButton = findViewById(R.id.guardarGuardiaButton);

        selectedDate = Calendar.getInstance();
        diaActuacionField.setText(sdf.format(selectedDate.getTime()));

        diaActuacionField.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate.getTime());

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        selectedDate = calendar;
                        diaActuacionField.setText(sdf.format(selectedDate.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setCalendarViewShown(true);
            datePickerDialog.getDatePicker().setSpinnersShown(false);

            datePickerDialog.show();
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

        // Validate date
        if (selectedDate == null) {
            ToastHelper.info(this, "Selecciona una fecha válida");
            return;
        }

        String diaActuacion = sdf.format(selectedDate.getTime());
        Log.d("Guardia", "Fecha seleccionada: " + diaActuacion);

        Guardia guardia = new Guardia();
        guardia.setNombreAsistido(nombre);
        guardia.setDiaActuacion(diaActuacion);
        guardia.setPorJuzgado(juzgado);
        guardia.setCobrado(cobrado);

        Log.d("Guardia", "Guardia a enviar: " +
                "nombre: " + guardia.getNombreAsistido() +
                ", fecha: " + guardia.getDiaActuacion() +
                ", juzgado: " + guardia.isPorJuzgado() +
                ", cobrado: " + guardia.isCobrado());

        apiService.createGuardia(guardia).enqueue(new Callback<Guardia>() {
            @Override
            public void onResponse(Call<Guardia> call, Response<Guardia> response) {
                if (response.isSuccessful()) {
                    Guardia creada = response.body();
                    Log.d("Guardia", "Guardada con éxito: " + creada);
                    NotificationHelper.Notification(GuardiaActivity.this, "Guardia registrada",
                            "Guardia guardada correctamente para " + nombre);
                    ToastHelper.info(GuardiaActivity.this, "Guardia guardada correctamente");

                    Intent intent = new Intent(GuardiaActivity.this, SituacionGuardiaActivity.class);
                    intent.putExtra("guardia_id", creada.getId());
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
                    NotificationHelper.Notification(GuardiaActivity.this, "Error en guardia",
                            "Error al guardar la guardia. Código: " + response.code());
                    ToastHelper.error(GuardiaActivity.this, "Error al guardar guardia. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Guardia> call, Throwable t) {
                Log.e("Guardia-FALLO", "Fallo de red: " + t.getMessage(), t);
                NotificationHelper.Notification(GuardiaActivity.this, "Error de red",
                        "Fallo de red al guardar la guardia: " + t.getMessage());
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