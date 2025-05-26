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

public class RecursoGuardiaActivity extends AppCompatActivity {

    EditText expedienteRecursoField;
    Switch switchResuelto;
    Button guardarButton, cancelarButton, btnIrRecursoExtra;
    int guardiaId;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_guardia);

        expedienteRecursoField = findViewById(R.id.nExpedienteRecursoField);
        switchResuelto = findViewById(R.id.switchResueltoRecurso);
        guardarButton = findViewById(R.id.guardarRecursoGuardiaButton);
        cancelarButton = findViewById(R.id.cancelarRecursoGuardiaButton);
        btnIrRecursoExtra = findViewById(R.id.btnIrRecursoExtra);

        dbh = new DatabaseHelper(this);
        guardiaId = getIntent().getIntExtra("guardia_id", -1);

        if (guardiaId == -1) {
            Toast.makeText(this, "Guardia no recibida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbh.obtenerRecursoPorId(guardiaId);
        if (cursor != null && cursor.moveToFirst()) {
            expedienteRecursoField.setText(cursor.getString(0));
            actualizarEstadoSwitch(switchResuelto, cursor.getInt(1) == 1, "Resolución", "Pendiente");
            cursor.close();
        }

        switchResuelto.setOnCheckedChangeListener((btn, check) ->
                actualizarEstadoSwitch(switchResuelto, check, "Resolución", "Pendiente"));

        guardarButton.setOnClickListener(v -> {
            String expediente = expedienteRecursoField.getText().toString().trim();
            int resuelto = switchResuelto.isChecked() ? 1 : 0;

            boolean ok = dbh.insertarActualizarRecursoGuardia(guardiaId, expediente, resuelto);
            if (ok) {
                Toast.makeText(this, "Recurso guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar recurso", Toast.LENGTH_SHORT).show();
            }
        });

        cancelarButton.setOnClickListener(v -> finish());

        btnIrRecursoExtra.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecursoExtraOrdinarioActivity.class);
            intent.putExtra("guardia_id", guardiaId);
            startActivity(intent);
        });

    }

    private void actualizarEstadoSwitch(Switch s, boolean check, String on, String off) {
        s.setChecked(check);
        s.setText(check ? on : off);
        s.setTextColor(check ? Color.GREEN : Color.RED);
    }
}
