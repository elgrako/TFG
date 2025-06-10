package com.example.tfg.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tfg.MyApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://34.230.71.133/";
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
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
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
