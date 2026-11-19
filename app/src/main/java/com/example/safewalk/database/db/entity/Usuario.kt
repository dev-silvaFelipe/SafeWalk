package com.example.safewalk.database.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa os usuários do SafeWalk.
 *
 * @property id Identificador único gerado automaticamente pelo Room.
 * @property nome Nome completo do cidadão ou denominação do órgão público.
 * @property numTel Telefone para autenticação e contato.
 * @property senhaHash Senha ou credencial de acesso.
 * @property isPrefeitura Indica se a conta possui privilégios institucionais de prefeitura (gestão urbana).
 * @property createdAt Timestamp de criação do registro no banco local.
 */
@Entity("usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val numTel: String,
    val senhaHash: String,

    val isPrefeitura: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()
)
