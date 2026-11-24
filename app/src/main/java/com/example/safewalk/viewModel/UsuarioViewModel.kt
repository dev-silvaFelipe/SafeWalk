package com.example.safewalk.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safewalk.database.db.entity.Usuario
import com.example.safewalk.database.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    fun cadastrarUsuario(
        nome: String,
        telefone: String,
        senha: String,
        onResult: () -> Unit
    ) {
        viewModelScope.launch {
            val usuario = Usuario(
                nome = nome,
                numTel = telefone,
                senhaHash = senha,
                isPrefeitura = false
            )
            repository.salvarUsuario(usuario)
            onResult()
        }
    }

    fun login(
        numTel: String,
        senha: String,
        onResult: (Usuario?) -> Unit
    ) {
        viewModelScope.launch {
            val usuario = repository.buscarPorTelefone(numTel)
            if (usuario != null && usuario.senhaHash == senha) {
                onResult(usuario)
            } else {
                onResult(null)
            }
        }
    }

    /**
     * Efetua o login direto como Prefeitura Municipal utilizando o perfil pré-selecionado.
     */
    fun loginPrefeitura(onResult: (Usuario) -> Unit) {
        viewModelScope.launch {
            val prefeitura = repository.obterOuCriarPrefeitura()
            onResult(prefeitura)
        }
    }
}
