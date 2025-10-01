package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.tfg.local.entity.ApelacionGuardia;

import java.util.List;

@Dao
public interface ApelacionGuardiaDao {

    @Query("SELECT * FROM ApelacionGuardia WHERE deletedAt IS NULL ORDER BY updatedAt DESC")
    List<ApelacionGuardia> getAll();

    @Query("SELECT * FROM ApelacionGuardia WHERE id = :id LIMIT 1")
    ApelacionGuardia findById(String id);

    @Query("SELECT * FROM ApelacionGuardia WHERE guardiaId = :guardiaId AND deletedAt IS NULL LIMIT 1")
    ApelacionGuardia findByGuardia(String guardiaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ApelacionGuardia> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ApelacionGuardia item);

    @Update
    void update(ApelacionGuardia item);

    @Upsert
    void upsert(ApelacionGuardia item);

    @Delete
    void delete(ApelacionGuardia item);

    @Query("UPDATE ApelacionGuardia SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);

    @Query("DELETE FROM ApelacionGuardia")
    void deleteAll();
}
