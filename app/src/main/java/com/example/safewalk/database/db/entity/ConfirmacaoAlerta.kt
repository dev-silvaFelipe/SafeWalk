package com.example.safewalk.database.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    "confirmacoes",
    foreignKeys = [
        ForeignKey(
            entity = AlertaInfraestrutura::class,
            parentColumns = ["id"],
            childColumns = ["alertaID"],
                    onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioI"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["alertaId", "usuarioId"], unique = true)
    ]
)
data class ConfirmacaoAlerta(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,

    val alertaId: Int,
    val usuarioId:Int
)
