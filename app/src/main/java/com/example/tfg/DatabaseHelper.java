package com.example.tfg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tfgDatabase.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE Datos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE," +
                "dni TEXT," +
                "nExpediente TEXT," +
                "euros REAL," +
                "presentado INTEGER DEFAULT 0," +
                "validado INTEGER DEFAULT 0," +
                "pagado INTEGER DEFAULT 0," +
                "nTalon INTEGER," +
                "comentarios TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Datos");
        onCreate(db);
    }

    public boolean insertarDatosEdit(Datos datos) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String query = "INSERT INTO Datos (nombre, dni, nExpediente, euros) VALUES (?, ?, ?, ?)";
            db.execSQL(query, new Object[]{datos.getNombre(), datos.getDni(), datos.getnExpediente(), datos.getEuros()
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public Cursor obtenerDatos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nombre, dni, nExpediente, euros FROM Datos", null);
    }
    public Cursor obtenerSituacion1(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT presentado, validado, pagado, nTalon, comentarios FROM Datos WHERE nombre = ?", new String[]{nombre});
    }


    public boolean updateSituacion1(String nombre, int presentado, int validado, int pagado, int nTalon, String comentarios) {
        SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE Datos SET presentado = ?, validado = ?, pagado = ?, nTalon = ?, comentarios = ? WHERE nombre = ?";
            db.execSQL(query, new Object[]{presentado, validado, pagado, nTalon, comentarios, nombre});
            return true;
    }
}
