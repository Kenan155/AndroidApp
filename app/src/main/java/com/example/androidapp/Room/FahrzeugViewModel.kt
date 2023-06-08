package com.example.androidapp.Room

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.Entity.Fahrzeug
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FahrzeugViewModel(
    private val dao: FahrzeugDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.MARKE)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.MARKE -> dao.getContactsOrderedByFirstName()
                SortType.NAME -> dao.getContactsOrderedByLastName()
                SortType.PS -> dao.getContactsOrderedByPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(FahrzeugState())
    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            fahrzeuge = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FahrzeugState())

    fun onEvent(event: FahrzeugEvent) {
        when(event) {
            is FahrzeugEvent.DeleteFahrzeug -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }
            FahrzeugEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingFahrzeug = false
                ) }
            }
            FahrzeugEvent.SaveFahrzeug -> {
                val marke = state.value.marke
                val name = state.value.name
                val ps = state.value.ps
                val fotoURL = state.value.fotoURL

                if(marke.isBlank() || name.isBlank() || ps.isBlank()) {
                    return
                }

                val contact = Fahrzeug(
                    marke = marke,
                    name = name,
                    ps = ps,
                    fotoURL = fotoURL,
                )
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                _state.update { it.copy(
                    isAddingFahrzeug = false,
                    marke = "",
                    name = "",
                    ps = "",
                    fotoURL = "",
                ) }
            }
            is FahrzeugEvent.SetMarke -> {
                _state.update { it.copy(
                    marke = event.marke
                ) }
            }
            is FahrzeugEvent.SetName -> {
                _state.update { it.copy(
                    name = event.name
                ) }
            }
            is FahrzeugEvent.SetPS -> {
                _state.update { it.copy(
                    ps = event.ps
                ) }
            }
            is FahrzeugEvent.SetFotoURL -> {
                _state.update { it.copy(
                    fotoURL = event.fotoURL
                ) }
            }
            FahrzeugEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingFahrzeug = true
                ) }
            }
            is FahrzeugEvent.SortContacts -> {
                _sortType.value = event.sortType
            }
        }
    }
}