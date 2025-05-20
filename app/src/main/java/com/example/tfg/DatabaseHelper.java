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

        String createSituacionGuardia = "CREATE TABLE IF NOT EXISTS SituacionGuardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreAsistido TEXT UNIQUE," +
                "comentarios TEXT," +
                "nTalon TEXT," +
                "euros TEXT," +
                "presentado INTEGER DEFAULT 0," +
                "validado INTEGER DEFAULT 0," +
                "pagado INTEGER DEFAULT 0)";
        db.execSQL(createSituacionGuardia);

        String createApelacionGuardiaTable = "CREATE TABLE ApelacionGuardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nExpediente TEXT," +
                "admitido INTEGER," +
                "presentado INTEGER," +
                "sentencia INTEGER)";
        db.execSQL(createApelacionGuardiaTable);

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

    public boolean insertarSituacionGuardia(String nombre, String comentarios, String nTalon, String euros, int presentado, int validado, int pagado) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SituacionGuardia WHERE nombreAsistido = ?", new String[]{nombre});
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (exists) {
            String query = "UPDATE SituacionGuardia SET comentarios = ?, nTalon = ?, euros = ?, presentado = ?, validado = ?, pagado = ? WHERE nombreAsistido = ?";
            db.execSQL(query, new Object[]{comentarios, nTalon, euros, presentado, validado, pagado, nombre});
        } else {
            String query = "INSERT INTO SituacionGuardia (nombreAsistido, comentarios, nTalon, euros, presentado, validado, pagado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(query, new Object[]{nombre, comentarios, nTalon, euros, presentado, validado, pagado});
        }
        return true;
    }

    public Cursor obtenerSituacionGuardia(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT comentarios, nTalon, euros, presentado, validado, pagado FROM SituacionGuardia WHERE nombreAsistido = ?", new String[]{nombre});
    }


    public boolean insertarApelacionGuardia(String nExpediente, boolean admitido, boolean presentado, boolean sentencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = admitido ? 1 : 0;
        int p = presentado ? 1 : 0;
        int s = sentencia ? 1 : 0;

        try {
            String query = "INSERT INTO ApelacionGuardia (nExpediente, admitido, presentado, sentencia) VALUES (?, ?, ?, ?)";
            db.execSQL(query, new Object[]{nExpediente, a, p, s});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public Cursor obtenerApelacionesGuardia() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM ApelacionGuardia", null);
    }


}
