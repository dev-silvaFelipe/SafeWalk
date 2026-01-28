package com.example.safewalk.database.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val numTel: String,
    val senhaHash: String,

    val createdAt: Long = System.currentTimeMillis()
)
