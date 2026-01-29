package com.example.safewalk.database.db.converters

import androidx.room.TypeConverter
import com.example.safewalk.database.db.entity.enums.StatusAlerta
import com.example.safewalk.database.db.entity.enums.TipoAlerta

class EnumConverters {

    @TypeConverter
    fun fromTipoAlerta(tipo: TipoAlerta): String{
        return tipo.name
    }

    @TypeConverter
    fun toTipoAlerta(valor: String): TipoAlerta{
        return TipoAlerta.valueOf(valor)
    }

    @TypeConverter
    fun fromStatusAlerta(status: StatusAlerta): String{
        return status.name
    }

    @TypeConverter
    fun toStatusAlerta(valor: String): StatusAlerta{
        return StatusAlerta.valueOf(valor)
    }
}