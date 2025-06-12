package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.tfg.local.entity.Guardia;

import java.util.List;

@Dao
public interface GuardiaDao {
    @Query("SELECT * FROM guardia WHERE deletedAt is null ORDER BY diaActuacion Desc")
    List<Guardia> getAll();

    @Query("Select * from guardia where id = :id limit 1")
    Guardia findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Guardia> itemlist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Guardia item);

    @Update
    void update(Guardia item);

    @Delete
    void delete(Guardia item);

    @Query("DELETE FROM guardia")
    void deleteAll();

    @Query("UPDATE Guardia SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);
}