package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.tfg.local.entity.Guardia;
import com.example.tfg.local.entity.Registro;

import java.util.List;

@Dao
public interface RegistroDao {

    @Query("SELECT * FROM Registro WHERE deletedAt IS NULL ORDER BY updatedAt DESC")
    List<Registro> getAll();

    @Query("SELECT * FROM Registro WHERE id = :id LIMIT 1")
    Registro getById(String id);

    @Query("SELECT * FROM Registro WHERE nombre = :nombre LIMIT 1")
    Registro getByNombre(String nombre);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Registro item);

    @Update
    void update(Registro item);

    @Upsert
    void upsert(Registro entity);

    @Query("UPDATE Registro SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);

    @Query("DELETE FROM Registro")
    void deleteAll();
}
