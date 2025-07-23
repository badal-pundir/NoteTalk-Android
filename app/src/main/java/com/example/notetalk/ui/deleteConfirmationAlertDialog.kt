package com.example.notetalk.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.notetalk.data.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationAlertDialog(note: Note, noteToDelete: MutableState<Note?>, viewModel: NoteViewModel) {
//    val note by viewModel.allNotes.collectAsState()
//    val noteToDelete = remember { mutableStateOf<Note?>(null) }

    AlertDialog(
        onDismissRequest = { noteToDelete.value = null },
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this note?") },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteNote(note)
                    noteToDelete.value = null
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { noteToDelete.value = null }
                ) {
                Text("Cancel")
            }
        }
    )
}