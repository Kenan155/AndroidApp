package com.example.androidapp.Room

import androidx.room.*
import com.example.androidapp.Entity.Fahrzeug
import kotlinx.coroutines.flow.Flow

@Dao
interface FahrzeugDao {

    @Upsert
    suspend fun upsertFahrzeug(fahrzeug: Fahrzeug)

    @Delete
    suspend fun deleteFahrzeug(fahrzeug: Fahrzeug)

    @Query("SELECT * FROM fahrzeug ORDER BY marke ASC")
    fun getFahrzeugeOrderedByMarke(): Flow<List<Fahrzeug>>

    @Query("SELECT * FROM fahrzeug ORDER BY name ASC")
    fun getFahrzeugeOrderedByName(): Flow<List<Fahrzeug>>

    @Query("SELECT * FROM fahrzeug ORDER BY ps ASC")
    fun getFahrzeugeOrderedByPS(): Flow<List<Fahrzeug>>
}