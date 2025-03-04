package com.example.tfg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tfgDatabase.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateDatabase = "Create Table Datos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre varchar(255) UNIQUE," +
                "nExpediente varchar(255)," +
                "euros decimal(10,2)," +
                "presentado boolean default 0," +
                "validado boolean default 0," +
                "nTalon integer)";
        db.execSQL(CreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Datos");
        onCreate(db);
    }


    public boolean insertarDatos(String nombre, String nExpediente, double euros, int nTalon) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Datos (nombre, nExpediente, euros, nTalon) VALUES (?,?,?,?)";
        db.execSQL(query, new Object[]{nombre, nExpediente, euros, nTalon});
        return true;
    }

    public Cursor obtenerDatos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Datos", null);
    }
}

