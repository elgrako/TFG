package com.example.tfg.local.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(
        tableName = "RecursoGuardia",
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
public class RecursoGuardia {
    @PrimaryKey
    @NonNull
    public String id;
    @NonNull
    public String guardiaId;

    public String nExpediente;
    public boolean resuelto;

    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;
    public Long remoteId;
}
