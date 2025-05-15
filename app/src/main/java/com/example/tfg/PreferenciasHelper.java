package com.example.tfg;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciasHelper {

    private static final String PREF_NAME = "tfgPreferences";
    private static final String KEY_USUARIO = "userActivo";

    public static void guardarUsuario(Context context, String usuario) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USUARIO, usuario).apply();
    }

    public static String obtenerUsuario(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USUARIO, null);
    }

    public static void cerrarSesion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_USUARIO).apply();
    }

    public static boolean SesionIniciada(Context context) {
        return obtenerUsuario(context) != null;
    }
}
