package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.tfg.local.entity.SituacionGuardia;

import java.util.List;

@Dao
public interface SituacionGuardiaDao {


    @Query("SELECT * FROM SituacionGuardia WHERE deletedAt IS NULL")
    List<SituacionGuardia> getAll();

    @Query("SELECT * FROM SituacionGuardia WHERE id = :id LIMIT 1")
    SituacionGuardia getById(String id);

    @Query("SELECT * FROM SituacionGuardia WHERE guardiaId = :guardiaId AND deletedAt IS NULL LIMIT 1")
    SituacionGuardia findSituacionByGuardia(String guardiaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SituacionGuardia> itemlist);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SituacionGuardia item);

    @Update
    void update(SituacionGuardia item);

    @Delete
    void delete(SituacionGuardia item);

    @Query("DELETE FROM situacionguardia")
    void deleteAll();

    @Query("UPDATE SituacionGuardia SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);
}
