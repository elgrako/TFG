package com.example.tfg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tfgDatabase.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableDatos = "CREATE TABLE Datos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE," +
                "dni TEXT," +
                "nExpediente TEXT," +
                "euros REAL," +
                "email TEXT," +
                "telefono INTEGER," +
                "presentado INTEGER DEFAULT 0," +
                "validado INTEGER DEFAULT 0," +
                "pagado INTEGER DEFAULT 0," +
                "nTalon INTEGER," +
                "comentarios TEXT)";
        db.execSQL(createTableDatos);

        String createTableUsuarios = "CREATE TABLE Usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT UNIQUE," +
                "contrasena TEXT)";
        db.execSQL(createTableUsuarios);

        String createGuardiaTable = "CREATE TABLE Guardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreAsistido TEXT," +
                "diaActuacion TEXT," +
                "porJuzgado INTEGER DEFAULT 0," +
                "cobrado INTEGER DEFAULT 0)";
        db.execSQL(createGuardiaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Datos");
        onCreate(db);
    }

    public boolean insertarOActualizarDatosEdit(Datos datos) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM Datos WHERE nombre = ?", new String[]{datos.getNombre()});
        boolean existe = (cursor != null && cursor.moveToFirst());
        cursor.close();

        if (existe) {
            String query = "UPDATE Datos SET dni = ?, nExpediente = ?, euros = ?, email = ?, telefono = ? WHERE nombre = ?";
            db.execSQL(query, new Object[]{
                    datos.getDni(),
                    datos.getnExpediente(),
                    datos.getEuros(),
                    datos.getEmail(),
                    datos.getTelefono(),
                    datos.getNombre()
            });
        } else {
            String query = "INSERT INTO Datos (nombre, dni, nExpediente, euros, email, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            db.execSQL(query, new Object[]{
                    datos.getNombre(),
                    datos.getDni(),
                    datos.getnExpediente(),
                    datos.getEuros(),
                    datos.getEmail(),
                    datos.getTelefono()
            });
        }
        return true;
    }


    public Cursor obtenerDatosMain() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nombre, dni, nExpediente, euros FROM Datos", null);
    }

    public Cursor obtenerSituacion1(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT presentado, validado, pagado, nTalon, comentarios FROM Datos WHERE nombre = ?", new String[]{nombre});
    }

    public Cursor getExtrasByNombre(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT email, telefono FROM Datos WHERE nombre = ?", new String[]{nombre});
    }

    public boolean updateSituacion1(String nombre, int presentado, int validado, int pagado, int nTalon, String comentarios) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Datos SET presentado = ?, validado = ?, pagado = ?, nTalon = ?, comentarios = ? WHERE nombre = ?";
        db.execSQL(query, new Object[]{presentado, validado, pagado, nTalon, comentarios, nombre});
        return true;
    }

    public boolean insertarUsuario(String user, String passw) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Usuarios (usuario, contrasena) VALUES (?, ?)";
        db.execSQL(query, new Object[]{user, passw});
        return true;
    }

    public boolean checkLogin(String user, String passw) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE usuario = ? AND contrasena = ?", new String[]{user, passw});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean insertarGuardia(String nombreAsistido, String diaActuacion, boolean porJuzgado, boolean cobrado) {
        int porJuzgadoInt = 0;
        if (porJuzgado) porJuzgadoInt = 1;

        int cobradoInt = 0;
        if (cobrado) cobradoInt = 1;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Guardia (nombreAsistido, diaActuacion, porJuzgado, cobrado) VALUES (?, ?, ?, ?)";
        db.execSQL(query, new Object[]{
                nombreAsistido,
                diaActuacion,
                porJuzgadoInt,
                cobradoInt
        });
        return true;
    }

    public Cursor obtenerGuardias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Guardia", null);
    }


}
