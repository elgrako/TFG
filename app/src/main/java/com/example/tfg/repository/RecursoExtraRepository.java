package com.example.tfg.repository;

import android.content.Context;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.RecursoExtraOrdinario;

public class RecursoExtraRepository {
    private final AppDatabase db;
    private final String deviceId;

    public RecursoExtraRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public RecursoExtraOrdinario getByGuardia(String guardiaId){
        return db.recursoExtraOrdinarioDao().findByGuardia(guardiaId);
    }

    public String upsert(String guardiaId, Integer nExpediente, Boolean admitido){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            RecursoExtraOrdinario e = db.recursoExtraOrdinarioDao().findByGuardia(guardiaId);
            if (e == null){
                e = new RecursoExtraOrdinario();
                e.id = TimeUuid.uuid();
                e.guardiaId = guardiaId;
                e.deviceId = deviceId;
                e.createdAt = now;
                e.version = 1L;
            } else {
                e.version += 1L;
            }
            e.nExpediente = nExpediente;
            e.admitido = admitido;
            e.updatedAt = now;
            e.deletedAt = null;
            e.remoteId = null;

            db.recursoExtraOrdinarioDao().upsert(e);
            idOut[0] = e.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.recursoExtraOrdinarioDao().softDelete(id, TimeUuid.nowIso());
    }
}
