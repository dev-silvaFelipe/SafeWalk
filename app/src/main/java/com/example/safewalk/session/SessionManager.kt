package com.example.safewalk.session

/**
 * Gerenciador de sessão em memória do SafeWalk.
 *
 * Mantém o estado de autenticação do usuário ativo, permitindo identificar
 * se o perfil atual é um cidadão ou um representante oficial da Prefeitura.
 */
object SessionManager {
    var usuarioLogadoId: Int? = null
    var usuarioLogadoNome: String? = null
    var isPrefeitura: Boolean = false

    fun logout() {
        usuarioLogadoId = null
        usuarioLogadoNome = null
        isPrefeitura = false
    }
}
