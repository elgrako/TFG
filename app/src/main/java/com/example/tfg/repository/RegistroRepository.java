package com.example.tfg.repository;

import android.content.Context;

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

    public Registro getById(String id){
        return db.registroDao().getById(id);
    }

    public Registro getByNombre(String nombre){
        return db.registroDao().getByNombre(nombre);
    }

    public String upsertByNombre(String nombre, String dni, String nExpediente, Double euros,
                                 String email, String telefono,
                                 Boolean presentado, Boolean validado, Boolean pagado,
                                 Integer nTalon, String comentarios){
        final String now = TimeUuid.nowIso();
        final String[] idOut = new String[1];

        db.runInTransaction(() -> {
            Registro r = db.registroDao().getByNombre(nombre);
            if (r == null){
                r = new Registro();
                r.id = TimeUuid.uuid();
                r.nombre = nombre;
                r.createdAt = now;
                r.version = 1L;
                r.deviceId = deviceId;
            } else {
                r.version += 1L;
            }
            r.dni = dni;
            r.nExpediente = nExpediente;
            r.euros = euros;
            r.email = email;
            r.telefono = telefono;
            r.presentado = presentado;
            r.validado = validado;
            r.pagado = pagado;
            r.nTalon = nTalon;
            r.comentarios = comentarios;
            r.updatedAt = now;
            r.deletedAt = null;
            r.remoteId = null;

            db.registroDao().insert(r);
            idOut[0] = r.id;
        });
        return idOut[0];
    }

    public void softDelete(String id){
        db.registroDao().softDelete(id, TimeUuid.nowIso());
    }
}
