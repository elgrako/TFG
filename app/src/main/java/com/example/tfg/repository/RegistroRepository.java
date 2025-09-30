package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.R;
import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.Registro;

import java.util.List;

public class RegistroRepository {

    private final AppDatabase db;
    private final String deviceId;

    public RegistroRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<Registro> list(){
        return db.registroDao().getAll();
    }

    public void create(String nombre, String dni, String nExpediente, Double euros, String email, String telefono){
        Registro r = new Registro();
        r.id = TimeUuid.uuid();
        r.nombre = nombre;
        r.dni = dni;
        r.nExpediente = nExpediente;
        r.euros = euros;
        r.email = email;
        r.telefono = telefono;

        String now = TimeUuid.nowIso();
        r.deviceId = deviceId;
        r.createdAt = now;
        r.updatedAt = now;
        r.deletedAt = null;
        r.version = 1L;
        r.remoteId = null;

        db.registroDao().upsert(r);
    }

    public void softDelete(String id){
        db.registroDao().softDelete(id, TimeUuid.nowIso());
    }
}
