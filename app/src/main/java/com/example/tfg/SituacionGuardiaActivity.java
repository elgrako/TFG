package com.example.tfg;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SituacionGuardiaActivity extends AppCompatActivity {

    EditText comentariosField, nTalonField, eurosField;
    Switch presentadoSwitch, validadoSwitch, pagadoSwitch;
    Button cancelarButton, guardarButton, btnIrApelacion;
    DatabaseHelper dbh;
    int guardiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacion_guardia);

        dbh = new DatabaseHelper(this);

        comentariosField = findViewById(R.id.comentariosGuardiaField);
        nTalonField = findViewById(R.id.nTalonGuardiaField);
        eurosField = findViewById(R.id.eurosGuardiaField);
        presentadoSwitch = findViewById(R.id.switchPresentadoGuardia);
        validadoSwitch = findViewById(R.id.switchValidadoGuardia);
        pagadoSwitch = findViewById(R.id.switchPagadoGuardia);
        cancelarButton = findViewById(R.id.cancelarSituacionGuardiaButton);
        guardarButton = findViewById(R.id.guardarSituacionGuardiaButton);
        btnIrApelacion = findViewById(R.id.btnIrApelacion);

        guardiaId = getIntent().getIntExtra("guardia_id", -1);

        if (guardiaId == -1) {
            Toast.makeText(this, "Error al recibir la guardia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbh.obtenerSituacionGuardiaPorId(guardiaId);
        if (cursor != null && cursor.moveToFirst()) {
            comentariosField.setText(cursor.getString(0));
            nTalonField.setText(cursor.getString(1));
            eurosField.setText(cursor.getString(2));
            actualizarEstadoSwitch(presentadoSwitch, cursor.getInt(3) == 1, "Presentado", "Pendiente");
            actualizarEstadoSwitch(validadoSwitch, cursor.getInt(4) == 1, "Validado", "Por Validar");
            actualizarEstadoSwitch(pagadoSwitch, cursor.getInt(5) == 1, "Pagado", "Por Pagar");
            cursor.close();
        }

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

        guardarButton.setOnClickListener(v -> {
            String comentarios = comentariosField.getText().toString().trim();
            String nTalon = nTalonField.getText().toString().trim();
            String euros = eurosField.getText().toString().trim();

            int presentado = 0;
            if (presentadoSwitch.isChecked()) presentado = 1;
            int validado = 0;
            if (validadoSwitch.isChecked()) validado = 1;
            int pagado = 0;
            if (pagadoSwitch.isChecked()) pagado = 1;

            boolean ok = dbh.insertarSituacionGuardiaPorId(guardiaId, comentarios, nTalon, euros, presentado, validado, pagado);
            if (ok) {
                Toast.makeText(this, "Situación guardada", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar situación", Toast.LENGTH_SHORT).show();
            }
        });

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String textoOn, String textoOff) {
        s.setChecked(check);
        s.setText(check ? textoOn : textoOff);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
