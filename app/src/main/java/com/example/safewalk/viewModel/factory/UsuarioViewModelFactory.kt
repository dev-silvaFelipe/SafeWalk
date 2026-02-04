package com.example.safewalk.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safewalk.database.repository.UsuarioRepository
import com.example.safewalk.viewModel.UsuarioViewModel

class UsuarioViewModelFactory(
    private val repository: UsuarioRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel inválido")
    }
}
