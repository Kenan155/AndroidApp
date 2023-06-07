package com.example.androidapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidapp.Room.FahrzeugEvent
import com.example.androidapp.Room.FahrzeugState
import com.example.androidapp.R
import com.example.androidapp.Room.SortType

@Composable
fun FahrzeugScreen(
    state: FahrzeugState,
    onEvent: (FahrzeugEvent) -> Unit
) {
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Scaffold() { _ ->
        if(state.isAddingFahrzeug) {
            AddContactDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(FahrzeugEvent.SortContacts(sortType))
                                },
                            verticalAlignment = CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(FahrzeugEvent.SortContacts(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }
            item {
                TextField(
                    value = searchQuery,
                    onValueChange = setSearchQuery,
                    label = { Text(text = "Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 1.dp)
                )
            }
            val filteredContacts = if (searchQuery.isBlank()) {
                state.fahrzeuge
            } else {
                state.fahrzeuge.filter { fahrzeug ->
                    fahrzeug.name.contains(searchQuery, ignoreCase = true)
                }
            }
            items(filteredContacts) { fahrzeug ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(1.dp)
                ) {
                    Column {
                        Box(modifier = Modifier.aspectRatio(16f / 9f)) {
                            Image(
                                painter = painterResource(R.drawable.porsche),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${fahrzeug.marke} ${fahrzeug.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "PS: " + fahrzeug.ps,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            // Add more text or other content here if needed
                        }
                    }
                }
            }
        }
    }
}