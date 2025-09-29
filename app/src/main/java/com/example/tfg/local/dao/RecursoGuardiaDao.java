package com.example.tfg.local.dao;

import androidx.room.*;
import com.example.tfg.local.entity.RecursoGuardia;

@Dao
public interface RecursoGuardiaDao {
    @Query("SELECT * FROM RecursoGuardia WHERE guardiaId = :guardiaId AND deletedAt IS NULL LIMIT 1")
    RecursoGuardia findByGuardia(String guardiaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(RecursoGuardia item);

    @Update
    void update(RecursoGuardia item);

    @Query("UPDATE RecursoGuardia SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);

    @Query("DELETE FROM RecursoGuardia")
    void deleteAll();
}
