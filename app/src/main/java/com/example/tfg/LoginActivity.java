package com.example.tfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText userField, passField;
    Button loginButton, registerButton;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PreferenciasHelper.SesionIniciada(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        userField = findViewById(R.id.loginUserField);
        passField = findViewById(R.id.loginPassField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        dbh = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String user = userField.getText().toString().trim();
            String pass = passField.getText().toString().trim();

            if (dbh.checkLogin(user, pass)) {
                savePreferences(user);
                PreferenciasHelper.guardarUsuario(this, user);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

    }

    private void savePreferences(String username) {
        SharedPreferences prefs = getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuario", username);
        editor.apply();
    }
}
