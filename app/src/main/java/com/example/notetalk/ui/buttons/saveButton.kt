package com.example.notetalk.ui.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.notetalk.data.Note
import com.example.notetalk.ui.NoteViewModel


@Composable
fun EnabledSaveButton(viewModel: NoteViewModel, navController: NavHostController, noteToSave: Note) {
    val isTextFieldEmpty = noteToSave.title.isEmpty() || noteToSave.content.isEmpty()
    Button(
        onClick = {
            viewModel.addNote(noteToSave)
            navController.popBackStack()
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !isTextFieldEmpty
    ) {
        Text(text = "Save")
    }
}