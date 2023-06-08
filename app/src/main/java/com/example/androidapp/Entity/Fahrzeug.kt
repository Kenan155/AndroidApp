package com.example.androidapp.Entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fahrzeug(
    val marke: String,
    val name: String,
    val ps: String,
    val fotoURL: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
