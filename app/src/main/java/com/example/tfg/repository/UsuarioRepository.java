package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.Usuario;

public class UsuarioRepository {
    private final AppDatabase db;
    private final String deviceId;

    public UsuarioRepository(Context ctx) {
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public void create(String nombre, String email){
        Usuario u = new Usuario();
        u.id = TimeUuid.uuid();
        u.nombre =  nombre;
        u.email = email;

        String now = TimeUuid.nowIso();
        u.deviceId = deviceId;
        u.createdAt = now;
        u.updatedAt = now;
        u.deletedAt = null;
        u.version = 1L;
        u.remoteId = null;

        db.usuarioDao().upsert(u);
    }

    public void softDelete(String id){
        db.usuarioDao().softDelete(id, TimeUuid.nowIso());
    }
}
