package com.example.androidapp.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fahrzeug(
    val marke: String,
    val name: String,
    val ps: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
