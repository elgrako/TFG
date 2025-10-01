package com.example.tfg.repository;

import android.content.Context;

import com.example.tfg.core.DeviceId;
import com.example.tfg.core.TimeUuid;
import com.example.tfg.local.db.AppDatabase;
import com.example.tfg.local.entity.Usuario;

import java.util.List;

public class UsuarioRepository {
    private final AppDatabase db;
    private final String deviceId;

    public UsuarioRepository(Context ctx){
        this.db = AppDatabase.get(ctx);
        this.deviceId = DeviceId.get(ctx);
    }

    public List<Usuario> list(){ return db.usuarioDao().getAll(); }

    public Usuario getByEmail(String email){ return db.usuarioDao().getByEmail(email); }

    public String upsertByEmail(String nombre, String email){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            Usuario u = db.usuarioDao().getByEmail(email);
            if (u == null){
                u = new Usuario();
                u.id = TimeUuid.uuid();
                u.createdAt = now;
                u.version = 1L;
                u.deviceId = deviceId;
            } else {
                u.version += 1L;
            }
            u.nombre = nombre;
            u.email = email;
            u.updatedAt = now;
            u.deletedAt = null;
            u.remoteId = null;

            db.usuarioDao().insert(u);
            idOut[0] = u.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.usuarioDao().softDelete(id, TimeUuid.nowIso());
    }
}
