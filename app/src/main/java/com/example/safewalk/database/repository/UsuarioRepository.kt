package com.example.safewalk.database.repository

import com.example.safewalk.database.db.dao.UsuarioDao
import com.example.safewalk.database.db.entity.Usuario

class UsuarioRepository (
    private val usuarioDao: UsuarioDao
){
    suspend fun salvarUsuario( usuario: Usuario){
        usuarioDao.inserir(usuario)
    }

    suspend fun buscarPorTelefone(numTel: String): Usuario?{
        return usuarioDao.buscarPorTel(numTel)
    }

}