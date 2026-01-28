package com.example.safewalk.database.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.safewalk.database.db.entity.enums.StatusAlerta
import com.example.safewalk.database.db.entity.enums.TipoAlerta


@Entity(
    "alertas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioId"])]
)
data class AlertaInfraestrutura(
    @PrimaryKey(autoGenerate =true)
    val id: Int =0,

    val titulo: String,
    val descricao: String,

    val latitude: Double,
    val longitude: Double,

    val tipo: TipoAlerta,

    val confirmacoes: Int=1,

    val status: StatusAlerta = StatusAlerta.ATIVO,

    val usuarioId: Int,

    val createdAt: Long = System.currentTimeMillis()
)
