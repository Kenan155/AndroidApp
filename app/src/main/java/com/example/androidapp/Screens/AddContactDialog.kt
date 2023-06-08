package com.example.androidapp.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapp.Room.FahrzeugEvent
import com.example.androidapp.Room.FahrzeugState

@Composable
fun AddContactDialog(
    state: FahrzeugState,
    onEvent: (FahrzeugEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    if (selectedImageUri != null) {
        onEvent(FahrzeugEvent.SetFotoURL(selectedImageUri.toString()))
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(FahrzeugEvent.HideDialog)
        },
        title = { Text(text = "Fahrzeug inserieren") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Pick one photo")
                }
                TextField(
                    value = state.marke,
                    onValueChange = {
                        onEvent(FahrzeugEvent.SetMarke(it))
                    },
                    placeholder = {
                        Text(text = "Marke")
                    }
                )
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(FahrzeugEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Name")
                    }
                )
                TextField(
                    value = state.ps,
                    onValueChange = {
                        onEvent(FahrzeugEvent.SetPS(it))
                    },
                    placeholder = {
                        Text(text = "PS")
                    }
                )
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(FahrzeugEvent.SaveFahrzeug)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}