package com.example.tfg.repository;

import android.content.Context;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.ApelacionGuardia;

public class ApelacionRepository {
    private final AppDatabase db;
    private final String deviceId;

    public ApelacionRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public ApelacionGuardia getByGuardia(String guardiaId){
        return db.apelacionDao().findByGuardia(guardiaId);
    }

    public String upsert(String guardiaId, String nExpediente,
                         boolean admitido, boolean presentado, boolean sentencia){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            ApelacionGuardia a = db.apelacionDao().findByGuardia(guardiaId);
            if (a == null){
                a = new ApelacionGuardia();
                a.id = TimeUuid.uuid();
                a.guardiaId = guardiaId;
                a.deviceId = deviceId;
                a.createdAt = now;
                a.version = 1L;
            } else {
                a.version += 1L;
            }
            a.nExpediente = nExpediente;
            a.admitido = admitido;
            a.presentado = presentado;
            a.sentencia = sentencia;
            a.updatedAt = now;
            a.deletedAt = null;
            a.remoteId = null;

            db.apelacionDao().insert(a);
            idOut[0] = a.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.apelacionDao().softDelete(id, TimeUuid.nowIso());
    }
}
