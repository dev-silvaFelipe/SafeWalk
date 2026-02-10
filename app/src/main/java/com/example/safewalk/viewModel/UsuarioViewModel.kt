package com.example.safewalk.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safewalk.database.db.entity.Usuario
import com.example.safewalk.database.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    fun salvarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            repository.salvarUsuario(usuario)
        }
    }
}
