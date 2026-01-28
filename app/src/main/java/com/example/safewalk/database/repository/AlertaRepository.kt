package com.example.safewalk.database.repository

import com.example.safewalk.database.db.dao.AlertaDao
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.enums.StatusAlerta
import kotlinx.coroutines.flow.Flow


class AlertaRepository(
    private val alertaDao: AlertaDao
) {

    suspend fun criarAlerta(alerta: AlertaInfraestrutura) {
        alertaDao.inserir(alerta)
    }

    fun listarTodos(): Flow<List<AlertaInfraestrutura>> {
        return alertaDao.listarAlertas()
    }

    fun listarAtivos(): Flow<List<AlertaInfraestrutura>> {
        return alertaDao.listarPorStatus(StatusAlerta.ATIVO)
    }

    suspend fun confirmarAlerta(alertaId: Int) {
        alertaDao.confirmar(alertaId)
    }

    suspend fun encerrarAlerta(alertaId: Int) {
        alertaDao.atualizarStatus(alertaId, StatusAlerta.RESOLVIDO)
    }
}


