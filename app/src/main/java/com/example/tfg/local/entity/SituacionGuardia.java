package com.example.tfg.local.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "SituacionGuardia",
        foreignKeys = @ForeignKey(
                entity = Guardia.class,
                parentColumns = "id",
                childColumns = "guardiaid",
                onDelete = CASCADE
        ),

        indices = {
                @Index(value = "guardiaid", unique = true),
                @Index("updatedAt"),
                @Index("deletedAt")
        }
)
public class SituacionGuardia {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public String guardiaId;

    public String nTalon;
    public String euros;
    public String comentarios;

    public boolean presentado;
    public boolean validado;
    public boolean pagado;

    public String deviceId;
    public String createdAt;
    public String updatedAt;
    public String deletedAt;
    public long version;

    public String remoteId;
}