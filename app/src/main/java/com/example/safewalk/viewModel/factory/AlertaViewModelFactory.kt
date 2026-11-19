package com.example.safewalk.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.AlertaViewModel

class AlertaViewModelFactory(
    private val repository: AlertaRepository,
    private val usuarioId: Int,
    private val isPrefeitura: Boolean = false
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertaViewModel(repository, usuarioId, isPrefeitura) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
