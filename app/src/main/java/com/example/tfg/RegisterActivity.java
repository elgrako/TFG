package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText userField, passField;
    Button registerButton, backButton;
    DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbh = new DatabaseHelper(this);
        userField = findViewById(R.id.registerUserField);
        passField = findViewById(R.id.registerPassField);
        registerButton = findViewById(R.id.registerConfirmButton);
        backButton = findViewById(R.id.backToLoginButton);

        registerButton.setOnClickListener(v -> {
            String user = userField.getText().toString().trim();
            String pass = passField.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                ToastHelper.info(this, "Rellena todos los campos");
            }

            if (dbh.insertarUsuario(user, pass)) {
                Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                ToastHelper.error(this, "Usuario ya existente o error");
            }
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
