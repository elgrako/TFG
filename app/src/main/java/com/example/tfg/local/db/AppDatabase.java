// com.example.tfg.local.db.AppDatabase
package com.example.tfg.local.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tfg.local.dao.*;
import com.example.tfg.local.entity.*;

@Database(
        entities = {
                Guardia.class, SituacionGuardia.class, ApelacionGuardia.class,
                RecursoGuardia.class, RecursoExtraOrdinario.class,
                Registro.class, Usuario.class
        },
        version = 1, exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GuardiaDao guardiaDao();
    public abstract SituacionGuardiaDao situacionGuardiaDao();
    public abstract ApelacionGuardiaDao apelacionDao();
    public abstract RecursoGuardiaDao recursoGuardiaDao();
    public abstract RecursoExtraDao recursoExtraOrdinarioDao();
    public abstract RegistroDao registroDao();
    public abstract UsuarioDao usuarioDao();

    private static volatile AppDatabase INSTANCE;
    public static AppDatabase get(Context ctx){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    AppDatabase.class,
                                    "tfgDatabase.db"
                            )
                            .fallbackToDestructiveMigration() // cámbialo por Migrations cuando fijes esquema
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
