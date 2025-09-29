package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.tfg.local.entity.Guardia;

import java.util.List;

@Dao
public interface GuardiaDao {

    @Query("SELECT * FROM guardia WHERE deletedAt IS NULL ORDER BY diaActuacion DESC")
    List<Guardia> getAll();

    @Query("SELECT * FROM guardia WHERE id = :id LIMIT 1")
    Guardia findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Guardia> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Guardia item);

    @Update
    void update(Guardia item);

    @Delete
    void delete(Guardia item);

    @Upsert
    void upsert(Guardia entity);

    @Upsert
    void upsert(List<Guardia> entities);

    @Query("DELETE FROM guardia")
    void deleteAll();

    @Query("UPDATE guardia SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);
}
