package com.example.tfg;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApelacionGuardiaActivity extends AppCompatActivity {

    Switch switchAdmitido, switchPresentado, switchSentencia;
    EditText expedienteField;
    Button guardarButton, cancelarButton;
    DatabaseHelper dbh;

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

        dbh = new DatabaseHelper(this);

        cargarDatosSiExisten();

        switchAdmitido.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchAdmitido, checked, "Admitido", "Rechazado"));

        switchPresentado.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchPresentado, checked, "Presentado", "Pendiente"));

        switchSentencia.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchSentencia, checked, "Sentencia", "Apelación"));

        guardarButton.setOnClickListener(v -> {
            String expediente = expedienteField.getText().toString().trim();
            boolean admitido = switchAdmitido.isChecked();
            boolean presentado = switchPresentado.isChecked();
            boolean sentencia = switchSentencia.isChecked();

            if (expediente.isEmpty()) {
                Toast.makeText(this, "Introduce el número de expediente", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean insertado = dbh.insertarApelacionGuardia(expediente, admitido, presentado, sentencia);
            if (insertado) {
                Toast.makeText(this, "Apelación guardada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void cargarDatosSiExisten() {
        Cursor cursor = dbh.obtenerApelacionesGuardia();
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String expediente = cursor.getString(cursor.getColumnIndex("nExpediente"));
            @SuppressLint("Range") int admitido = cursor.getInt(cursor.getColumnIndex("admitido"));
            @SuppressLint("Range") int presentado = cursor.getInt(cursor.getColumnIndex("presentado"));
            @SuppressLint("Range") int sentencia = cursor.getInt(cursor.getColumnIndex("sentencia"));

            expedienteField.setText(expediente);
            actualizarEstadoSwitch(switchAdmitido, admitido == 1, "Admitido", "Rechazado");
            actualizarEstadoSwitch(switchPresentado, presentado == 1, "Presentado", "Pendiente");
            actualizarEstadoSwitch(switchSentencia, sentencia == 1, "Sentencia", "Apelación");

            cursor.close();
        } else {
            actualizarEstadoSwitch(switchAdmitido, false, "Admitido", "Rechazado");
            actualizarEstadoSwitch(switchPresentado, false, "Presentado", "Pendiente");
            actualizarEstadoSwitch(switchSentencia, false, "Sentencia", "Apelación");
        }
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String TextYes, String TextNo) {
        s.setChecked(check);
        s.setText(check ? TextYes : TextNo);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
