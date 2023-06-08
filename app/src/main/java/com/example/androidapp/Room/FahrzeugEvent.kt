package com.example.androidapp.Room

import android.net.Uri
import com.example.androidapp.Entity.Fahrzeug

sealed interface FahrzeugEvent {
    object SaveFahrzeug: FahrzeugEvent
    data class SetMarke(val marke: String): FahrzeugEvent
    data class SetName(val name: String): FahrzeugEvent
    data class SetPS(val ps: String): FahrzeugEvent
    data class SetFotoURL(val fotoURL: String): FahrzeugEvent
    object ShowDialog: FahrzeugEvent
    object HideDialog: FahrzeugEvent
    data class SortContacts(val sortType: SortType): FahrzeugEvent
    data class DeleteFahrzeug(val contact: Fahrzeug): FahrzeugEvent
}