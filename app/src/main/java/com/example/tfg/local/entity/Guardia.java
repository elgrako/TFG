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

    public String deviceId;          // Identificador del móvil
    public String createdAt;         // Fecha creación (ISO)
    public String updatedAt;         // Fecha última actualización
    public String deletedAt;         // null = vivo, fecha = borrado lógico
    public long version;             // Número de versión local

    public String remoteId;
}
