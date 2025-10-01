package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.tfg.local.entity.RecursoExtraOrdinario;

import java.util.List;

@Dao
public interface RecursoExtraDao {

    @Query("SELECT * FROM RecursoExtraOrdinario WHERE deletedAt IS NULL ORDER BY updatedAt DESC")
    List<RecursoExtraOrdinario> getAll();

    @Query("SELECT * FROM RecursoExtraOrdinario WHERE id = :id LIMIT 1")
    RecursoExtraOrdinario getById(String id);

    @Query("SELECT * FROM RecursoExtraOrdinario WHERE guardiaId = :guardiaId AND deletedAt IS NULL")
    RecursoExtraOrdinario findByGuardia(String guardiaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecursoExtraOrdinario item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RecursoExtraOrdinario> items);

    @Update
    void update(RecursoExtraOrdinario item);

    @Upsert
    void upsert(RecursoExtraOrdinario item);
    @Delete
    void delete(RecursoExtraOrdinario item);

    @Query("UPDATE RecursoExtraOrdinario SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);

    @Query("DELETE FROM RecursoExtraOrdinario")
    void deleteAll();
}
