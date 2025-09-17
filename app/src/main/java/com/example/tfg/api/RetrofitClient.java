package com.example.tfg.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tfg.Helpers.MyApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://54.158.194.13/";
    private static RetrofitClient instance;
    private final ApiService api;

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        SharedPreferences prefs = MyApp.getContext()
                                .getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
                        String token = prefs.getString("token", null);

                        Request.Builder builder = chain.request().newBuilder();
                        if (token != null) {
                            Log.d("RetrofitClient", "Token usado: " + token);
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    }
                })
                .build();

        Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(field -> {
                    if (field.getName().equals("nExpediente")) {
                        return "nExpediente";
                    }
                    return field.getName();
                })
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, typeOfT, context) -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_DATE))
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) instance = new RetrofitClient();
        return instance;
    }

    public ApiService getApi() {
        return api;
    }
}
