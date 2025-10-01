package com.example.tfg.repository;

import android.content.Context;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.SituacionGuardia;

public class SituacionGuardiaRepository {
    private final AppDatabase db;
    private final String deviceId;

    public SituacionGuardiaRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public SituacionGuardia getByGuardia(String guardiaId){
        return db.situacionGuardiaDao().findSituacionByGuardia(guardiaId);
    }

    public String upsert(String guardiaId, String comentarios, String nTalon, String euros,
                         boolean presentado, boolean validado, boolean pagado){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            SituacionGuardia s = db.situacionGuardiaDao().findSituacionByGuardia(guardiaId);
            if (s == null){
                s = new SituacionGuardia();
                s.id = TimeUuid.uuid();
                s.guardiaId = guardiaId;
                s.deviceId = deviceId;
                s.createdAt = now;
                s.version = 1L;
            } else {
                s.version += 1L;
            }
            s.comentarios = comentarios;
            s.nTalon = nTalon;
            s.euros = euros;
            s.presentado = presentado;
            s.validado = validado;
            s.pagado = pagado;
            s.updatedAt = now;
            s.deletedAt = null;
            s.remoteId = null;

            db.situacionGuardiaDao().insert(s);
            idOut[0] = s.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.situacionGuardiaDao().softDelete(id, TimeUuid.nowIso());
    }
}
