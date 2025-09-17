/**
package com.example.tfg;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tfgDatabase.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");

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

        String createSituacionGuardia = "CREATE TABLE SituacionGuardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardia_id INTEGER UNIQUE," +
                "comentarios TEXT," +
                "nTalon TEXT," +
                "euros TEXT," +
                "presentado INTEGER DEFAULT 0," +
                "validado INTEGER DEFAULT 0," +
                "pagado INTEGER DEFAULT 0," +
                "FOREIGN KEY(guardia_id) REFERENCES Guardia(id) ON DELETE CASCADE)";
        db.execSQL(createSituacionGuardia);

        String createApelacionGuardiaTable = "CREATE TABLE ApelacionGuardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardia_id INTEGER," +
                "nExpediente TEXT," +
                "admitido INTEGER," +
                "presentado INTEGER," +
                "sentencia INTEGER," +
                "FOREIGN KEY(guardia_id) REFERENCES Guardia(id) ON DELETE CASCADE)";
        db.execSQL(createApelacionGuardiaTable);

        String createRecursoGuardiaTable = "CREATE TABLE RecursoGuardia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardia_id INTEGER," +
                "nExpediente TEXT," +
                "resuelto INTEGER," +
                "FOREIGN KEY(guardia_id) REFERENCES Guardia(id) ON DELETE CASCADE)";
        db.execSQL(createRecursoGuardiaTable);

        String createRecursoExtraOrdinarioTable = "CREATE TABLE RecursoExtraOrdinario (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardia_id INTEGER," +
                "nExpediente INTEGER," +
                "admitido INTEGER," +
                "FOREIGN KEY(guardia_id) REFERENCES Guardia(id) ON DELETE CASCADE)";
        db.execSQL(createRecursoExtraOrdinarioTable);

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Datos");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Guardia");
        db.execSQL("DROP TABLE IF EXISTS SituacionGuardia");
        db.execSQL("DROP TABLE IF EXISTS ApelacionGuardia");
        db.execSQL("DROP TABLE IF EXISTS RecursoGuardia");
        db.execSQL("DROP TABLE IF EXISTS RecursoExtraOrdinario");

        onCreate(db);
    }

    public boolean insertarOActualizarDatosEdit(Registro registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM Datos WHERE nombre = ?", new String[]{registro.getNombre()});
        boolean existe = (cursor != null && cursor.moveToFirst());
        cursor.close();

        if (existe) {
            String query = "UPDATE Datos SET dni = ?, nExpediente = ?, euros = ?, email = ?, telefono = ? WHERE nombre = ?";
            db.execSQL(query, new Object[]{
                    registro.getDni(),
                    registro.getnExpediente(),
                    registro.getEuros(),
                    registro.getEmail(),
                    registro.getTelefono(),
                    registro.getNombre()
            });
        } else {
            String query = "INSERT INTO Datos (nombre, dni, nExpediente, euros, email, telefono) VALUES (?, ?, ?, ?, ?, ?)";
            db.execSQL(query, new Object[]{
                    registro.getNombre(),
                    registro.getDni(),
                    registro.getnExpediente(),
                    registro.getEuros(),
                    registro.getEmail(),
                    registro.getTelefono()
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


    public Cursor obtenerGuardias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Guardia", null);
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


    public Cursor obtenerSituacionGuardiaPorId(int guardiaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT comentarios, nTalon, euros, presentado, validado, pagado FROM SituacionGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
    }

    public boolean insertarSituacionGuardiaPorId(int guardiaId, String comentarios, String nTalon, String euros, int presentado, int validado, int pagado) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SituacionGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (exists) {
            String query = "UPDATE SituacionGuardia SET comentarios = ?, nTalon = ?, euros = ?, presentado = ?, validado = ?, pagado = ? WHERE guardia_id = ?";
            db.execSQL(query, new Object[]{comentarios, nTalon, euros, presentado, validado, pagado, guardiaId});
        } else {
            String query = "INSERT INTO SituacionGuardia (guardia_id, comentarios, nTalon, euros, presentado, validado, pagado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(query, new Object[]{guardiaId, comentarios, nTalon, euros, presentado, validado, pagado});
        }
        return true;
    }


    public Cursor obtenerApelacionGuardiaPorId(int guardiaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nExpediente, admitido, presentado, sentencia FROM ApelacionGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
    }

    public boolean insertaroActualizarApelacionGuardia(int guardiaId, String nExpediente, boolean admitido, boolean presentado, boolean sentencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ApelacionGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (exists) {
            db.execSQL("UPDATE ApelacionGuardia SET nExpediente = ?, admitido = ?, presentado = ?, sentencia = ? WHERE guardia_id = ?",
                    new Object[]{nExpediente, admitido, presentado, sentencia, guardiaId});
        } else {
            db.execSQL("INSERT INTO ApelacionGuardia (guardia_id, nExpediente, admitido, presentado, sentencia) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{guardiaId, nExpediente, admitido, presentado, sentencia});
        }
        return true;
    }


    public Cursor obtenerRecursoPorId(int guardiaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nExpediente, resuelto FROM RecursoGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
    }

    public boolean insertarActualizarRecursoGuardia(int guardiaId, String nExpediente, int resuelto) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RecursoGuardia WHERE guardia_id = ?", new String[]{String.valueOf(guardiaId)});
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (exists) {
            db.execSQL("UPDATE RecursoGuardia SET nExpediente = ?, resuelto = ? WHERE guardia_id = ?",
                    new Object[]{nExpediente, resuelto, guardiaId});
        } else {
            db.execSQL("INSERT INTO RecursoGuardia (guardia_id, nExpediente, resuelto) VALUES (?, ?, ?)",
                    new Object[]{guardiaId, nExpediente, resuelto});
        }
        return true;
    }


    public Cursor obtenerRecursoExtraOrdinarioPorId(int guardiaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT nExpediente, admitido FROM RecursoExtraOrdinario WHERE guardia_id = ?",
                new String[]{String.valueOf(guardiaId)});
    }

    public boolean insertarActualizarRecursoExtraOrdinario(int guardiaId, int nExpediente, int admitido) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RecursoExtraOrdinario WHERE guardia_id = ?",
                new String[]{String.valueOf(guardiaId)});
        boolean existe = cursor.moveToFirst();
        cursor.close();

        if (existe) {
            db.execSQL("UPDATE RecursoExtraOrdinario SET nExpediente = ?, admitido = ? WHERE guardia_id = ?",
                    new Object[]{nExpediente, admitido, guardiaId});
        } else {
            db.execSQL("INSERT INTO RecursoExtraOrdinario (guardia_id, nExpediente, admitido) VALUES (?, ?, ?)",
                    new Object[]{guardiaId, nExpediente, admitido});
        }
        return true;
    }

    public boolean borrarJudicialPorNombre(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("Datos", "nombre = ?", new String[]{nombre});
        return rows > 0;
    }
    public boolean borrarGuardiaPorId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("Guardia", "id = " + id, null);
        return rows > 0;
    }
}
 **/