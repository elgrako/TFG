package com.example.tfg.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "guardia",
        indices = {@Index("updatedAt"), @Index("deletedAt")}
)
public class Guardia {
    @PrimaryKey @NonNull
    public String id;

    public String nombreAsistido;
    public String diaActuacion;
    public boolean porJuzgado;
    public boolean cobrado;

    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;

    public String remoteId;
}
