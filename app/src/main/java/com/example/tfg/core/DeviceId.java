package com.example.tfg.core;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

public class DeviceId {
    private static final String PREF = "core_ids";
    private static final String KEY = "device_id";
    public static String get(Context ctx){
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String v = sp.getString(KEY, null);
        if (v == null){
            v = UUID.randomUUID().toString();
            sp.edit().putString(KEY, v).apply();
        }
        return v;
    }
}
