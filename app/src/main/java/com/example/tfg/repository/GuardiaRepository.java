package com.example.tfg.repository;

import android.content.Context;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.Guardia;

import java.util.List;

public class GuardiaRepository {
    private final AppDatabase db;
    private final String deviceId;

    public GuardiaRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<Guardia> list(){
        return db.guardiaDao().getAll();
    }

    public Guardia getById(String id){
        return db.guardiaDao().findById(id);
    }

    public String create(String nombreAsistido, String diaIso, boolean porJuzgado, boolean cobrado){
        Guardia g = new Guardia();
        g.id = TimeUuid.uuid();
        g.nombreAsistido = nombreAsistido;
        g.diaActuacion = diaIso;
        g.porJuzgado = porJuzgado;
        g.cobrado = cobrado;

        String now = TimeUuid.nowIso();
        g.deviceId = deviceId;
        g.createdAt = now;
        g.updatedAt = now;
        g.deletedAt = null;
        g.version = 1L;
        g.remoteId = null;

        db.guardiaDao().upsert(g);
        return g.id;
    }

    public void update(Guardia g){
        g.updatedAt = TimeUuid.nowIso();
        g.version += 1L;
        db.guardiaDao().update(g);
    }

    public void softDelete(String id){
        db.guardiaDao().softDelete(id, TimeUuid.nowIso());
    }

    public void hardDeleteAll(){
        db.guardiaDao().deleteAll();
    }
}
