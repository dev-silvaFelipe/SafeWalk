package com.example.safewalk.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.repository.AlertaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertaViewModel(
    private val repository: AlertaRepository,
    private val usuarioId: Int,
    private val isPrefeitura: Boolean = false
) : ViewModel() {

    // Se for Prefeitura, lista todos os chamados ativos da cidade para auditoria e resolução;
    // Se for cidadão, lista apenas os chamados ativos abertos pelo próprio usuário (ocorrências resolvidas somem do painel).
    val alertas = (if (isPrefeitura) {
        repository.listarAtivos()
    } else {
        repository.listarAtivosPorUsuario(usuarioId)
    }).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun criarAlerta(alerta: AlertaInfraestrutura) {
        viewModelScope.launch {
            repository.criarAlerta(alerta)
        }
    }

    fun confirmarAlerta(alertaId: Int) {
        viewModelScope.launch {
            repository.confirmarAlerta(alertaId)
        }
    }

    fun encerrarAlerta(alertaId: Int) {
        viewModelScope.launch {
            repository.encerrarAlerta(alertaId)
        }
    }
}
