package com.example.safewalk.database.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safewalk.database.db.entity.AlertaInfraestrutura

@Dao
interface AlertaDao {
    @Insert
    suspend fun inserir(alerta: AlertaInfraestrutura)

    @Query("SELECT * FROM alertas WHERE ativo =1 ORDER BY createdAt DESC")
    suspend fun listarAtivos(): List<AlertaInfraestrutura>

    @Query("UPDATE alertas SET confirmacoes = confirmacoes + 1 WHERE id = :alertaId")
    suspend fun confrimar(alertaId: Int)

    @Query("UPDATE alertas SET ativo = 0 WHERE  id = :alertaId")
    suspend fun encerrar(alertaId: Int)
}