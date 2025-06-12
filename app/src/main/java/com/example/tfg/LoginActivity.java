package com.example.tfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.Helpers.PreferenciasHelper;
import com.example.tfg.Helpers.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class LoginActivity extends AppCompatActivity {

    EditText userField, passField;
    Button loginButton, registerButton;
    final String API_URL = "http://54.158.194.13/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PreferenciasHelper.sesionIniciada(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        userField = findViewById(R.id.loginUserField);
        passField = findViewById(R.id.loginPassField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> {
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
                        Log.e("Login", "Fallo conexión: ", e);
                        runOnUiThread(() -> ToastHelper.error(LoginActivity.this, "Error de conexión"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            Log.d("LoginActivity", "Respuesta del servidor: " + responseBody);
                            JSONObject responseJson = null;
                            try {
                                responseJson = new JSONObject(responseBody);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String token = null;
                            try {
                                token = responseJson.getString("token");
                                Log.d("LoginActivity", "Token recibido: " + token);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            saveToken(user, token);
                            runOnUiThread(() -> {
                                PreferenciasHelper.guardarUsuario(LoginActivity.this, user);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> ToastHelper.error(LoginActivity.this, "Credenciales inválidas"));
                        }
                    }
                });

            } catch (Exception e) {
                ToastHelper.error(this, "Error en datos de login");
            }
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void saveToken(String username, String token) {
        SharedPreferences prefs = getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuario", username);
        editor.putString("token", token);
        editor.apply();
    }
}
