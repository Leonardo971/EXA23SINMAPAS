package com.example.leona.exa23.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "empleado")
data class EmpleadoEntry (
        @PrimaryKey()
        var id:Long = 0 ,
        var Nombre:String,
        var Apellido: String,
        var edad: Float,
        var Domicilio:String,
        var nomencargado: String,
        var Ayuda: Float
)