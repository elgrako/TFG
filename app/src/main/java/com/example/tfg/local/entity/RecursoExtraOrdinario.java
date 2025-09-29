package com.example.tfg.local.entity;

import static androidx.room.ForeignKey.CASCADE;
import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(
        tableName = "RecursoExtraOrdinario",
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
public class RecursoExtraOrdinario {
    @PrimaryKey @NonNull public String id;
    @NonNull public String guardiaId;

    public Integer nExpediente;
    public Boolean admitido;

    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;
    public Long remoteId;
}
