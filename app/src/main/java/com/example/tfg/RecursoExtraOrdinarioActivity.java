package com.example.tfg;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecursoExtraOrdinarioActivity extends AppCompatActivity {

    EditText expedienteField;
    Switch switchAdmitido;
    Button guardarButton, cancelarButton;
    DatabaseHelper dbh;
    int guardiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_extra_ordinario);

        expedienteField = findViewById(R.id.nExpedienteRecursoExtraField);
        switchAdmitido = findViewById(R.id.switchAdmitidoRecursoExtra);
        guardarButton = findViewById(R.id.guardarRecursoExtraButton);
        cancelarButton = findViewById(R.id.cancelarRecursoExtraButton);

        dbh = new DatabaseHelper(this);
        guardiaId = getIntent().getIntExtra("guardia_id", -1);

        if (guardiaId == -1) {
            ToastHelper.info(this, "Guardia no encontrada");
            finish();
            return;
        }

        cargarDatosSiExisten();

        switchAdmitido.setOnCheckedChangeListener((btn, checked) ->
                actualizarEstadoSwitch(switchAdmitido, checked, "Admitido", "Rechazado"));

        guardarButton.setOnClickListener(v -> {
            String exp = expedienteField.getText().toString().trim();
            if (exp.isEmpty()) {
                ToastHelper.info(this, "Introduce el número de expediente");
                return;
            }

            int nExpediente = Integer.parseInt(exp);
            int admitido = switchAdmitido.isChecked() ? 1 : 0;

            boolean guardado = dbh.insertarActualizarRecursoExtraOrdinario(guardiaId, nExpediente, admitido);
            if (guardado) {
                ToastHelper.info(this, "Guardado correctamente");
                finish();
            } else {
                ToastHelper.error(this, "Error al guardar");
            }
        });

        cancelarButton.setOnClickListener(v -> finish());
    }

    private void cargarDatosSiExisten() {
        Cursor cursor = dbh.obtenerRecursoExtraOrdinarioPorId(guardiaId);
        if (cursor != null && cursor.moveToFirst()) {
            expedienteField.setText(String.valueOf(cursor.getInt(0)));
            actualizarEstadoSwitch(switchAdmitido, cursor.getInt(1) == 1, "Admitido", "Rechazado");
            cursor.close();
        } else {
            actualizarEstadoSwitch(switchAdmitido, false, "Admitido", "Rechazado");
        }
    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String textoOn, String textoOff) {
        s.setChecked(check);
        s.setText(check ? textoOn : textoOff);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
