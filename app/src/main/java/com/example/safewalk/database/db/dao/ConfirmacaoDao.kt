package com.example.safewalk.database.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.safewalk.database.db.entity.ConfirmacaoAlerta

@Dao
interface ConfirmacaoDao {

   @Insert
   suspend fun inserir(confirmacao: ConfirmacaoAlerta)

   @Query("""
       SELECT COUNT(*)
       FROM confirmacoes
       WHERE alertaId = :alertaId
       """)
   suspend fun contarConfirmacoes(alertaId: Int): Int
}