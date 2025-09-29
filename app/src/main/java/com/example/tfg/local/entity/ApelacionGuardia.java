package com.example.tfg.local.entity;

import static androidx.room.ForeignKey.CASCADE;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "ApelacionGuardia",
        foreignKeys = @ForeignKey(
                entity = Guardia.class,
                parentColumns = "id",
                childColumns = "guardiaId",
                onDelete = CASCADE
        ),
        indices = {
                @Index(value = "guardiaId", unique = true),
                @Index("updatedAt"),
                @Index("deletedAt")
        }
)
public class ApelacionGuardia {
    @PrimaryKey @NonNull public String id;
    @NonNull public String guardiaId;

    public String nExpediente;
    public boolean admitido;
    public boolean presentado;
    public boolean sentencia;

    // sync
    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;
    public Long remoteId;
}
