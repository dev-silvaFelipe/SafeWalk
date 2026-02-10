package com.example.safewalk.database.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.StatusAlerta
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {

    @Insert
    suspend fun inserir(alerta: AlertaInfraestrutura)

    @Query("""
        SELECT * FROM alertas 
        ORDER BY createdAt DESC
    """)
    fun listarAlertas(): Flow<List<AlertaInfraestrutura>>

    @Query("""
        SELECT * FROM alertas 
        WHERE status = :status 
        ORDER BY createdAt DESC
    """)
    fun listarPorStatus(status: StatusAlerta): Flow<List<AlertaInfraestrutura>>

    @Query("""
        UPDATE alertas 
        SET confirmacoes = confirmacoes + 1 
        WHERE id = :alertaId
    """)
    suspend fun confirmar(alertaId: Int)

    @Query("""
        UPDATE alertas 
        SET status = :status 
        WHERE id = :alertaId
    """)
    suspend fun atualizarStatus(
        alertaId: Int,
        status: StatusAlerta
    )
    @Query("SELECT * FROM alertas WHERE  usuarioId = :usuarioId ORDER BY createdAt DESC")
    fun listarPorUsuario(usuarioId: Int): Flow<List<AlertaInfraestrutura>>
}
