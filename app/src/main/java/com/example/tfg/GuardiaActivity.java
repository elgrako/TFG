package com.example.tfg;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GuardiaActivity extends AppCompatActivity {

    EditText diaField, nombreAsistidoField;
    Switch porJuzgadoSwitch, cobradoSwitch;
    Button guardarButton;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardia);

        dbh = new DatabaseHelper(this);

        diaField = findViewById(R.id.diaActuacionField);
        nombreAsistidoField = findViewById(R.id.nombreAsistidoField);
        porJuzgadoSwitch = findViewById(R.id.switchporJuzgado);
        cobradoSwitch = findViewById(R.id.switchcobrado);
        guardarButton = findViewById(R.id.guardarGuardiaButton);


        actualizarEstadoSwitch(porJuzgadoSwitch, false, "Sí por pasa Juzgado", "No por por Juzgado");
        actualizarEstadoSwitch(cobradoSwitch, false, "Cobrado", "No cobrado");

        porJuzgadoSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                actualizarEstadoSwitch(porJuzgadoSwitch, isChecked, "Sí por pasa Juzgado", "No por por Juzgado"));

        cobradoSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                actualizarEstadoSwitch(cobradoSwitch, isChecked, "Cobrado", "No cobrado"));

        guardarButton.setOnClickListener(v -> {
            String dia = diaField.getText().toString().trim();
            String nombre = nombreAsistidoField.getText().toString().trim();
            boolean juzgado = porJuzgadoSwitch.isChecked();
            boolean cobrado = cobradoSwitch.isChecked();

            if (!dia.isEmpty() && !nombre.isEmpty()) {
                boolean insertado = dbh.insertarGuardia(nombre, dia, juzgado, cobrado);
                if (insertado) {
                    Toast.makeText(this, "Guardia guardada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
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
