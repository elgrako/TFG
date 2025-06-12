package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tfg.local.entity.ApelacionGuardia;

import java.util.List;

@Dao
public interface ApelacionDao {
    @Query("SELECT * FROM apelacion ORDER BY id")
    List<ApelacionGuardia> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ApelacionGuardia> items);

    @Query("DELETE FROM apelacion")
    void deleteAll();
}