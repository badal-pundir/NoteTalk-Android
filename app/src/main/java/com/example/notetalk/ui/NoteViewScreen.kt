package com.example.notetalk.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.data.PreviewNoteRepository
import com.example.notetalk.ui.theme.NoteTalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetail(
    viewModel: NoteViewModel,
    navController: NavController,
    noteId: Int
) {
    // Observe the specific note from your ViewModel
    val note by viewModel.getNote(noteId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note") },
                navigationIcon = {
                    // Back button
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Edit button
                    IconButton(onClick = {
                        // Navigate to the edit screen, passing the current note's ID
                        navController.navigate("addEditNote/${note?.id}")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Note"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Use a let block to safely access the note's details only when it's not null
        note?.let { noteDetails ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Large, bold title
                item {
                    Text(
                        text = noteDetails.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Note content
                item {
                    Text(
                        text = noteDetails.content,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview(name = "Note Detail Screen", showBackground = true)
@Composable
fun NoteDetailPreview() {
    val repository = remember { PreviewNoteRepository() }
    val viewModel = remember { NoteViewModel(repository) }
    val navController = rememberNavController()

    NoteTalkTheme{
        NoteDetail(
            viewModel = viewModel,
            navController = navController,
            noteId = 1 // Use ID 1 to show the first note from our fake data
        )
    }
}

//