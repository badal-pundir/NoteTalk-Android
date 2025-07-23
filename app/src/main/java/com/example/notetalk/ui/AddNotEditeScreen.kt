package com.example.notetalk.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.data.Note
import com.example.notetalk.data.PreviewNoteRepository
import com.example.notetalk.ui.buttons.EnabledSaveButton
import com.example.notetalk.ui.theme.NoteTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNote(viewModel: NoteViewModel, navController: NavHostController, noteId: Int) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val isEditing = noteId != 0

    // note content taking too much time to load on the UI.
    // so we introducing a simple loading state. We will only show the input fields after
    // the LaunchedEffect has successfully populated the state variables.
    var isLoading by remember { mutableStateOf(isEditing)}
    if(isEditing) {
        Log.d("AddEditNote", "noteId: $noteId")
        // In case of StateFlow use collectAsStateWithLifecycle instead of collectAsState
        val note by viewModel.getNote(noteId).collectAsState(initial = null)
        // .collectAsState() to convert them into Compose states.
        /*LaunchedEffect(key1 = note) {
            note?.let {
                title = it.title
                content = it.content
            }
        }*/
        LaunchedEffect(key1 = note){
            /*
            * When you declare val note by viewModel.getNote..., you are creating a property with a
            * custom getter. The compiler is designed to be extra safe and assumes that another
            * thread could change the value of note right after your if (note != null) check.
            * Therefore, it can't "smart cast" it to a non-nullable type.
            * Solution:
            * Create a stable, local variable inside your LaunchedEffect. This captures the value
            * at that moment, allowing the compiler to perform the smart cast safely.
            */
            // Capturing the current value in a local variable.(currentNote)
            val currentNote = note
            if(currentNote != null) {
                title = currentNote.title
                content = currentNote.content
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing)"Edit Note" else "Add Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")

                    }
                }
            )
        }
    ) { paddingValues ->
        // Show a loading spinner or the content based on the isLoading state
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(16.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )
                Spacer(modifier = Modifier.padding(16.dp))
                val noteToSave = Note(id = noteId, title = title, content = content)
//            if(noteToSave.title.isNotEmpty() && noteToSave.content.isNotEmpty())
                /*
                Button(
                    onClick = {
    //                    val noteToSave = Note(id = noteId, title = title, content = content)
    //                    if(noteToSave.title.isNotEmpty() && noteToSave.content.isNotEmpty())
                            viewModel.addNote(noteToSave)
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save")
                }*/
                EnabledSaveButton(viewModel, navController, noteToSave)
            }
        }
    }
}

// Preview for adding a brand new note.
@Preview(name = "Add New Note Screen", showBackground = true)
@Composable
fun AddNotePreview() {
    val repository = remember { PreviewNoteRepository() }
    val viewModel = remember { NoteViewModel(repository) }
    val navController = rememberNavController()

    NoteTalkTheme {
        AddEditNote(
            viewModel = viewModel,
            navController = navController,
            noteId = 0 // Use ID 0 to signify a new note
        )
    }
}

// Preview for editing an existing note.
@Preview(name = "Edit Existing Note Screen", showBackground = true)
@Composable
fun EditNotePreview() {
    val repository = remember { PreviewNoteRepository() }
    val viewModel = remember { NoteViewModel(repository) }
    val navController = rememberNavController()

    NoteTalkTheme {
        AddEditNote(
            viewModel = viewModel,
            navController = navController,
            noteId = 2 // Use ID 2 to edit the second note from our fake data
        )
    }
}
