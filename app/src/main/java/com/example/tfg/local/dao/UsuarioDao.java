package com.example.tfg.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tfg.local.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE deletedAt IS NULL ORDER BY updatedAt DESC")
    List<Usuario> getAll();

    @Query("SELECT * FROM Usuario WHERE id = :id LIMIT 1")
    Usuario getById(String id);

    @Query("SELECT * FROM Usuario WHERE email = :email LIMIT 1")
    Usuario getByEmail(String email);

    @Query("SELECT * FROM Usuario WHERE nombre = :nombre LIMIT 1")
    Usuario getByNombre(String nombre);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Usuario u);

    @Update
    void update(Usuario u);

    @Query("UPDATE Usuario SET deletedAt = :ts, updatedAt = :ts, version = version + 1 WHERE id = :id")
    void softDelete(String id, String ts);

    @Query("DELETE FROM Usuario")
    void deleteAll();
}
