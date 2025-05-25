package com.example.tfg;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Currency;

public class Situacion1Activity extends AppCompatActivity {
    NotificationHelper nh;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_situacion1);

        nh = new NotificationHelper();

        Button finishButton = findViewById(R.id.next1SitButton);
        Button cancelButton = findViewById(R.id.cancel1SitButton);
        EditText Coments1Sit = findViewById(R.id.Coments1Sit);
        EditText NTalon1Sit = findViewById(R.id.NTalon1Sit);
        Switch Pendiente1Sit = findViewById(R.id.switchPendiente1Sit);
        Switch Validado1Sit = findViewById(R.id.switchValidado1Sit);
        Switch Pagado1Sit = findViewById(R.id.switchPagado1Sit2);
        TextView euros1Sit = findViewById(R.id.euros1Sit);

        Intent recoverIntent = getIntent();
        double euros = recoverIntent.getDoubleExtra("euros", 0.0);
        String nombre = recoverIntent.getStringExtra("nombre");

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("EUR"));
        String textoEuros = format.format(euros);
        euros1Sit.setText(textoEuros);

        dbh = new DatabaseHelper(this);
        Cursor cursor = dbh.obtenerSituacion1(nombre);
        if (cursor != null && cursor.moveToFirst()) {
            int presentado = cursor.getInt(0);
            int validado = cursor.getInt(1);
            int pagado = cursor.getInt(2);
            int nTalon = cursor.getInt(3);
            String comentarios = cursor.getString(4);

            actualizarEstadoSwitch(Pendiente1Sit, presentado == 1, "Aprobado", "Pendiente");
            actualizarEstadoSwitch(Validado1Sit, validado == 1, "Validado", "Por Validar");
            actualizarEstadoSwitch(Pagado1Sit, pagado == 1, "Pagado", "Por Pagar");

            NTalon1Sit.setText(String.valueOf(nTalon));
            Coments1Sit.setText(comentarios);

            cursor.close();
        }

        Pendiente1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            actualizarEstadoSwitch(Pendiente1Sit, isChecked, "Aprobado", "Pendiente");
        });

        Validado1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            actualizarEstadoSwitch(Validado1Sit, isChecked, "Validado", "Por Validar");
        });

        Pagado1Sit.setOnCheckedChangeListener((b, isChecked) -> {
            actualizarEstadoSwitch(Pagado1Sit, isChecked, "Pagado", "Por Pagar");
        });


        finishButton.setOnClickListener(v -> {
            int presentado = 0;
            if (Pendiente1Sit.isChecked()) presentado = 1;

            int validado = 0;
            if (Validado1Sit.isChecked()) validado = 1;

            int pagado = 0;
            if (Pagado1Sit.isChecked()) pagado = 1;

            String nTalonTexto = NTalon1Sit.getText().toString();
            int nTalon = 0;
            if (!nTalonTexto.isEmpty()) {
                try {
                    nTalon = Integer.parseInt(nTalonTexto);
                } catch (NumberFormatException ignored) {
                }
            }

            String comentarios = Coments1Sit.getText().toString();

            boolean updated = dbh.updateSituacion1(nombre, presentado, validado, pagado, nTalon, comentarios);

            if (updated) {
                nh.Notification(this, "Situación actualizada", "Cambios guardados para " + nombre);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                ToastHelper.error(this, "Error al actualizar o guardar");
            }
        });

        cancelButton.setOnClickListener(v -> {
            Intent cancelIntent = new Intent(this, MainActivity.class);
            startActivity(cancelIntent);
            finish();
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
