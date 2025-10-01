package com.example.tfg.repository;

import android.content.Context;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.RecursoGuardia;

public class RecursoGuardiaRepository {
    private final AppDatabase db;
    private final String deviceId;

    public RecursoGuardiaRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public RecursoGuardia getByGuardia(String guardiaId){
        return db.recursoGuardiaDao().findByGuardia(guardiaId);
    }

    public String upsert(String guardiaId, String nExpediente, boolean resuelto){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            RecursoGuardia r = db.recursoGuardiaDao().findByGuardia(guardiaId);
            if (r == null){
                r = new RecursoGuardia();
                r.id = TimeUuid.uuid();
                r.guardiaId = guardiaId;
                r.deviceId = deviceId;
                r.createdAt = now;
                r.version = 1L;
            } else {
                r.version += 1L;
            }
            r.nExpediente = nExpediente;
            r.resuelto = resuelto;
            r.updatedAt = now;
            r.deletedAt = null;
            r.remoteId = null;

            db.recursoGuardiaDao().upsert(r);
            idOut[0] = r.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.recursoGuardiaDao().softDelete(id, TimeUuid.nowIso());
    }
}
