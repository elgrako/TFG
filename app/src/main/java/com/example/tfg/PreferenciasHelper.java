package com.example.tfg;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciasHelper {

    private static final String PREF_NAME = "tfgPreferences";
    private static final String KEY_USUARIO = "userActivo";
    private static final String KEY_TOKEN = "userToken";


    public static void guardarSesion(Context context, String usuario, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USUARIO, usuario);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static void guardarUsuario(Context context, String usuario) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_USUARIO, usuario).apply();
    }

    public static String obtenerUsuario(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USUARIO, null);
    }

    public static String obtenerToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }

    public static void cerrarSesion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .remove(KEY_USUARIO)
                .remove(KEY_TOKEN)
                .apply();
    }

    public static boolean sesionIniciada(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USUARIO, null) != null &&
                prefs.getString(KEY_TOKEN, null) != null;
    }
}
