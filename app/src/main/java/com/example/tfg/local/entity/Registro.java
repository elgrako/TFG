package com.example.tfg.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Registro",
        indices = {
                @Index(value = {"nombre"}, unique = true),
                @Index("updatedAt"),
                @Index("deletedAt")
        }
)
public class Registro {

    @PrimaryKey
    @NonNull
    public String id;

    public String nombre;
    public String dni;
    public String nExpediente;
    public Double euros;
    public String email;
    public String telefono;

    public Boolean presentado;
    public Boolean validado;
    public Boolean pagado;

    public Integer nTalon;
    public String comentarios;

    // Sync
    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;

    public Long remoteId;
}
