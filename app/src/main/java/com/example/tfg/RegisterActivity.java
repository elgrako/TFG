package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class RegisterActivity extends AppCompatActivity {

    EditText userField, passField;
    Button registerButton, backButton;
    final String API_URL = "http://10.0.2.2:8080/auth/register"; // Cambia a tu IP real si usas dispositivo físico

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userField = findViewById(R.id.registerUserField);
        passField = findViewById(R.id.registerPassField);
        registerButton = findViewById(R.id.registerConfirmButton);
        backButton = findViewById(R.id.backToLoginButton);

        registerButton.setOnClickListener(v -> {
            String user = userField.getText().toString().trim();
            String pass = passField.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                ToastHelper.info(this, "Rellena todos los campos");
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("username", user);
                json.put("password", pass);
                JSONArray roles = new JSONArray();
                roles.put("ROLE_USUARIO");
                json.put("roles", roles);

                RequestBody body = RequestBody.create(
                        json.toString(),
                        MediaType.parse("application/json")
                );

                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(body)
                        .build();

                OkHttpClient client = new OkHttpClient();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(() -> ToastHelper.error(RegisterActivity.this, "Error de conexión con el servidor"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(() -> {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                ToastHelper.error(RegisterActivity.this, "Error al registrar. ¿Usuario ya existe?");
                            }
                        });
                    }
                });

            } catch (Exception e) {
                ToastHelper.error(this, "Error en datos");
            }
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
