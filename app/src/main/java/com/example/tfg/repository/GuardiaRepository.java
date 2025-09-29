
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

    public void create(String nombreAsistido, String diaIso, boolean porJuzgado, boolean cobrado){
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
    }

    public void softDelete(String id){
        db.guardiaDao().softDelete(id, TimeUuid.nowIso());
    }
}
