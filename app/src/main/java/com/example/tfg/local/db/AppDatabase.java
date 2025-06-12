package com.example.tfg.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tfg.local.dao.*;
import com.example.tfg.local.entity.ApelacionGuardia;
import com.example.tfg.local.entity.Guardia;
import com.example.tfg.local.entity.RecursoExtraOrdinario;
import com.example.tfg.local.entity.RecursoGuardia;
import com.example.tfg.local.entity.Registro;
import com.example.tfg.local.entity.SituacionGuardia;
import com.example.tfg.local.entity.Usuario;

// Sube version al añadir tablas o columnas
@Database(
        entities = {
                Registro.class,
                Usuario.class,
                Guardia.class,
                SituacionGuardia.class,
                ApelacionGuardia.class,
                RecursoGuardia.class,
                RecursoExtraOrdinario.class
        },
        version = 1,
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract RegistroDao datosDao();
    public abstract UsuarioDao usuarioDao();
    public abstract GuardiaDao guardiaDao();
    public abstract SituacionGuardiaDao situacionGuardiaDao();
    public abstract ApelacionGuardiaDao apelacionGuardiaDao();
    public abstract RecursoGuardiaDao recursoGuardiaDao();
    public abstract RecursoExtraOrdinarioDao recursoExtraOrdinarioDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Si vas a usar SQLCipher, aquí iría openHelperFactory(...)
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "tfg.db"
                            )
                            .fallbackToDestructiveMigration() // cámbialo por Migrations cuando consolides
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

