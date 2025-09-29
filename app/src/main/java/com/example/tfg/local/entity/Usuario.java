package com.example.tfg.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Usuario",
        indices = {
                @Index(value = {"nombre"}, unique = true),
                @Index(value = {"email"}, unique = true),
                @Index("updatedAt"),
                @Index("deletedAt")
        }
)
public class Usuario {

    @PrimaryKey
    @NonNull
    public String id;

    public String nombre;
    public String email;

    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;

    public Long remoteId;
}
