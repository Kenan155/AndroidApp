package com.example.androidapp.Room

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
    private val _fahrzeuge = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.MARKE -> dao.getFahrzeugeOrderedByMarke()
                SortType.NAME -> dao.getFahrzeugeOrderedByName()
                SortType.PS -> dao.getFahrzeugeOrderedByPS()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(FahrzeugState())
    val state = combine(_state, _sortType, _fahrzeuge) { state, sortType, fahrzeuge ->
        state.copy(
            fahrzeuge = fahrzeuge,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FahrzeugState())

    fun onEvent(event: FahrzeugEvent) {
        when(event) {
            is FahrzeugEvent.DeleteFahrzeug -> {
                viewModelScope.launch {
                    dao.deleteFahrzeug(event.fahrzeug)
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
                val preis = state.value.preis
                val standort = state.value.standort
                val ausstattung = state.value.ausstattung
                val zeitraum = state.value.zeitraum
                val fotoURL = state.value.fotoURL

                if(marke.isBlank() || name.isBlank()) {
                    return
                }

                val fahrzeug = Fahrzeug(
                    marke = marke,
                    name = name,
                    ps = ps,
                    preis = preis,
                    standort = standort,
                    ausstattung = ausstattung,
                    zeitraum = zeitraum,
                    fotoURL = fotoURL,
                )
                viewModelScope.launch {
                    dao.upsertFahrzeug(fahrzeug)
                }
                _state.update { it.copy(
                    isAddingFahrzeug = false,
                    marke = "",
                    name = "",
                    ps = 0,
                    preis = 0,
                    standort = "",
                    ausstattung = "",
                    zeitraum = "",
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
            is FahrzeugEvent.SetPreis -> {
                _state.update { it.copy(
                    preis = event.preis
                ) }
            }
            is FahrzeugEvent.SetStandort -> {
                _state.update { it.copy(
                    standort = event.standort
                ) }
            }
            is FahrzeugEvent.SetAusstattung -> {
                _state.update { it.copy(
                    ausstattung = event.ausstattung
                ) }
            }
            is FahrzeugEvent.SetZeitraum -> {
                _state.update { it.copy(
                    zeitraum = event.zeitraum
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
            is FahrzeugEvent.SortFahrzeuge -> {
                _sortType.value = event.sortType
            }
        }
    }
}