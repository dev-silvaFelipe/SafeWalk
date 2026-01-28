package com.example.safewalk.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.safewalk.database.db.converters.EnumConverters
import com.example.safewalk.database.db.dao.AlertaDao
import com.example.safewalk.database.db.dao.ConfirmacaoDao
import com.example.safewalk.database.db.dao.UsuarioDao
import com.example.safewalk.database.db.entity.AlertaInfraestrutura
import com.example.safewalk.database.db.entity.ConfirmacaoAlerta
import com.example.safewalk.database.db.entity.Usuario

@Database(
    entities = [
        AlertaInfraestrutura::class,
        Usuario::class,
        ConfirmacaoAlerta::class
    ],
    version = 1,
    exportSchema = false
)
    @TypeConverters(EnumConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alertaDao(): AlertaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun confirmacaoDao(): ConfirmacaoDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "safewalk_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}