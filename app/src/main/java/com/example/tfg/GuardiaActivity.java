package com.example.tfg;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GuardiaActivity extends AppCompatActivity {

    TextView diaField;
    EditText nombreAsistidoField;
    Switch porJuzgadoSwitch, cobradoSwitch;
    Button guardarButton;
    DatabaseHelper dbh;
    NotificationHelper nh;

    private Date selectedDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia);

        dbh = new DatabaseHelper(this);
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

        guardarButton.setOnClickListener(v -> {
            String nombre = nombreAsistidoField.getText().toString().trim();
            boolean juzgado = porJuzgadoSwitch.isChecked();
            boolean cobrado = cobradoSwitch.isChecked();

            if (nombre.isEmpty()) {
                ToastHelper.info(this, "Introduce el nombre del asistido");
                return;
            }

            String diaActuacion = sdf.format(selectedDate);
            boolean insertado = dbh.insertarGuardia(nombre, diaActuacion, juzgado, cobrado);

            if (insertado) {
                nh.Notification(this, "Guardia registrada", "Guardia guardada correctamente para " + nombre);
                Intent intent = new Intent(this, SituacionGuardiaActivity.class);
                intent.putExtra("nombreAsistido", nombre);
                startActivity(intent);
                finish();
            } else {
                ToastHelper.error(this, "Error al guardar la guardia");
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
