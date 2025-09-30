package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;

import com.example.tfg.local.entity.SituacionGuardia;

import java.util.List;

public class SituacionGuardiaRepository {
    private final AppDatabase db;
    private final String deviceId;

    public SituacionGuardiaRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<SituacionGuardia> list(){
        return db.situacionGuardiaDao().getAll();
    }

    public void create(String nTalon,String euros, boolean presentado, boolean validado, boolean pagado){
        SituacionGuardia sg = new SituacionGuardia();
        sg.id = TimeUuid.uuid();
        sg.nTalon = nTalon;
        sg.euros = euros;
        sg.presentado = presentado;
        sg.validado = validado;
        sg.pagado = pagado;

        String now = TimeUuid.nowIso();
        sg.deviceId = deviceId;
        sg.createdAt = now;
        sg.updatedAt = now;
        sg.deletedAt = null;
        sg.version = 1L;
        sg.remoteId = null;

        db.situacionGuardiaDao().upsert(sg);
    }
    public void softDelete(String id){
        db.situacionGuardiaDao().softDelete(id, TimeUuid.nowIso());
    }
}
