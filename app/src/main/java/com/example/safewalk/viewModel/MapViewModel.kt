package com.example.safewalk.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safewalk.database.repository.AlertaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: AlertaRepository
) : ViewModel() {

    val alertas = repository
        .listarAtivos()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
    fun confirmarAlerta(alertaId: Int) {
        viewModelScope.launch {
            repository.confirmarAlerta(alertaId)
        }
    }

}
