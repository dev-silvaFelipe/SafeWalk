package com.example.safewalk.database.repository

import com.example.safewalk.database.db.dao.UsuarioDao
import com.example.safewalk.database.db.entity.Usuario

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    suspend fun salvarUsuario(usuario: Usuario) {
        usuarioDao.inserir(usuario)
    }

    suspend fun buscarPorTelefone(numTel: String): Usuario? {
        return usuarioDao.buscarPorTel(numTel)
    }

    /**
     * Garante a existência do usuário oficial da Prefeitura para acesso rápido/pré-selecionado.
     * Retorna a entidade correspondente no banco.
     */
    suspend fun obterOuCriarPrefeitura(): Usuario {
        val telefonePrefeitura = "0800156"
        val existente = usuarioDao.buscarPorTel(telefonePrefeitura)
        if (existente != null) return existente

        val novaPrefeitura = Usuario(
            nome = "Prefeitura Municipal (Infraestrutura)",
            numTel = telefonePrefeitura,
            senhaHash = "admin123",
            isPrefeitura = true
        )
        usuarioDao.inserir(novaPrefeitura)
        return usuarioDao.buscarPorTel(telefonePrefeitura) ?: novaPrefeitura
    }
}
