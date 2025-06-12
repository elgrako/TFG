package com.example.tfg.backup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.dao.*;
import com.example.tfg.local.entity.ApelacionGuardia;
import com.example.tfg.local.entity.Guardia;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class RoomBackupDataProvider implements BackupDataProvider {

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    @Override
    public String exportToJson(@NonNull Context context) throws Exception {
        AppDatabase db = AppDatabase.getInstance(context);

        // 1) Lee TODO lo que quieras respaldar
        GuardiaDao guardiaDao = db.getGuardiaDao();
        ApelacionDao apelacionDao = db.getApelacionDao();

        List<Guardia> guardias = guardiaDao.getAll();        // <-- ajusta a tu DAO
        List<ApelacionGuardia> apelaciones = apelacionDao.getAll(); // <-- ajusta a tu DAO

        // 2) Construye el payload de backup con versión + datos
        BackupPayload payload = new BackupPayload();
        payload.version = backupVersion();
        payload.guardias = guardias;
        payload.apelaciones = apelaciones;

        // 3) Devuelve JSON (bonito y estable)
        return gson.toJson(payload);
    }

    @Override
    public void importFromJson(@NonNull Context context, @NonNull String json) throws Exception {
        AppDatabase db = AppDatabase.getInstance(context);

        // 1) Parsear el JSON
        BackupPayload payload = gson.fromJson(json, BackupPayload.class);
        if (payload == null) {
            throw new IllegalArgumentException("JSON de backup inválido o vacío");
        }

        // 2) Comprobar versión de formato (puedes ser más estricto si quieres)
        if (payload.version > backupVersion()) {
            throw new IllegalStateException(
                    "Versión de backup (" + payload.version + ") no soportada por la app (" + backupVersion() + ")"
            );
        }

        // 3) Importar dentro de una transacción para consistencia
        db.runInTransaction(() -> {
            GuardiaDao guardiaDao = db.getGuardiaDao();
            ApelacionDao apelacionDao = db.getApelacionDao();

            // ESTRATEGIA SIMPLE: "limpiar y restaurar"
            // Si prefieres "upsert" selectivo, mira el comentario de abajo.
            // Ordena por claves foráneas si las hay (primero padres, luego hijos).
            apelacionDao.deleteAll(); // si Apelacion depende de Guardia, borra hijos primero
            guardiaDao.deleteAll();

            if (payload.guardias != null && !payload.guardias.isEmpty()) {
                guardiaDao.insertAll(payload.guardias); // onConflict = REPLACE recomendado
            }
            if (payload.apelaciones != null && !payload.apelaciones.isEmpty()) {
                apelacionDao.insertAll(payload.apelaciones);
            }
        });
    }

    /**
     * Modelo JSON del backup. Añade aquí nuevas tablas cuando amplíes el respaldo.
     * Mantén los nombres estables: esto evita roturas entre versiones.
     */
    static class BackupPayload {
        int version;
        List<Guardia> guardias;
        List<ApelacionGuardia> apelaciones;
    }
}