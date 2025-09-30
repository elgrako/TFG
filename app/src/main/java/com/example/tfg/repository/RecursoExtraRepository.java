package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.RecursoExtraOrdinario;


import java.util.List;

public class RecursoExtraRepository {
    private final AppDatabase db;
    private final String deviceId;

    public RecursoExtraRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<RecursoExtraOrdinario> list(){
        return db.recursoExtraOrdinarioDao().getAll();
    }

    public void create(Integer nExpediente, boolean admitido){
        RecursoExtraOrdinario reo = new RecursoExtraOrdinario();
        reo.id = TimeUuid.uuid();
        reo.nExpediente = nExpediente;
        reo.admitido = admitido;

        String now = TimeUuid.nowIso();
        reo.deviceId = deviceId;
        reo.createdAt = now;
        reo.updatedAt = now;
        reo.deletedAt = null;
        reo.version = 1L;
        reo.remoteId = null;
    }

    public void softDelete(String id){
        db.recursoExtraOrdinarioDao().softDelete(id, TimeUuid.nowIso());
    }
}
