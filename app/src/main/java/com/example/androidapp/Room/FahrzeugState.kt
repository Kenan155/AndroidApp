package com.example.androidapp.Room

import android.net.Uri
import com.example.androidapp.Entity.Fahrzeug

data class FahrzeugState(
    val fahrzeuge: List<Fahrzeug> = emptyList(),
    val marke: String = "",
    val name: String = "",
    val ps: String = "",
    val fotoURL: String = "",
    val isAddingFahrzeug: Boolean = false,
    val sortType: SortType = SortType.MARKE
)
