package com.example.safewalk.database.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safewalk.database.db.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE numTel = :numTel LIMIT 1")
    suspend fun buscarPorTel(numTel: String): Usuario?
}