package com.example.safewalk.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safewalk.database.repository.AlertaRepository
import com.example.safewalk.viewModel.AlertaViewModel

class AlertaViewModelFactory(
    private val repository: AlertaRepository,
    private val usuarioId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertaViewModel::class.java)) {
            return AlertaViewModel(repository, usuarioId) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}

