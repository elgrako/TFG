package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.RecursoGuardia;
import com.example.tfg.local.entity.Registro;

import java.util.List;

public class RecursoGuardiaRepository {
    private final AppDatabase db;
    private final String deviceId;

    public RecursoGuardiaRepository(Context ctx) {
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<Registro> list() {
        return db.registroDao().getAll();
    }

    public void create(String nExpediente, boolean resuelto) {
        RecursoGuardia rg = new RecursoGuardia();
        rg.id = TimeUuid.uuid();
        rg.nExpediente = nExpediente;
        rg.resuelto = resuelto;

        String now = TimeUuid.nowIso();
        rg.deviceId = deviceId;
        rg.createdAt = now;
        rg.updatedAt = now;
        rg.deletedAt = null;
        rg.version = 1L;
        rg.remoteId = null;
    }

    public void softDelete(String id) {
        db.recursoGuardiaDao().softDelete(id, TimeUuid.nowIso());
    }
}
