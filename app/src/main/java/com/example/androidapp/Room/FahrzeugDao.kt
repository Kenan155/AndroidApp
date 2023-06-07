package com.example.androidapp.Room

import androidx.room.*
import com.example.androidapp.Entity.Fahrzeug
import kotlinx.coroutines.flow.Flow

@Dao
interface FahrzeugDao {

    @Upsert
    suspend fun upsertContact(contact: Fahrzeug)

    @Delete
    suspend fun deleteContact(contact: Fahrzeug)

    @Query("SELECT * FROM fahrzeug ORDER BY marke ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Fahrzeug>>

    @Query("SELECT * FROM fahrzeug ORDER BY name ASC")
    fun getContactsOrderedByLastName(): Flow<List<Fahrzeug>>

    @Query("SELECT * FROM fahrzeug ORDER BY ps ASC")
    fun getContactsOrderedByPhoneNumber(): Flow<List<Fahrzeug>>
}