package com.example.tfg.Helpers;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp instancia;

    @Override
    public void onCreate() {
        super.onCreate();
        instancia = this;
    }

    public static Context getContext() {
        return instancia.getApplicationContext();
    }
}
