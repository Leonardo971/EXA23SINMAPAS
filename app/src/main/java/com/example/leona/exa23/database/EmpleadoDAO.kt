package com.example.leona.exa23.database

import android.arch.persistence.room.*

@Dao
interface EmpleadoDAO {

    @Query("select * from empleado order by Nombre")
    fun loadAllEmpleado(): List<EmpleadoEntry>

    @Query("SELECT * FROM empleado WHERE id=:id")
    fun loadEmpleadoById(id: Long):EmpleadoEntry

    @Insert
    fun insertEmpleado(empleadoEntry: EmpleadoEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEmpleado(empleadoEntry: EmpleadoEntry)

    @Delete
    fun deleteEmpleado(empleadoEntry: EmpleadoEntry)
}