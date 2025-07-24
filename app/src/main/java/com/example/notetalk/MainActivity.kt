package com.example.notetalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.data.Note
import com.example.notetalk.data.Theme
import com.example.notetalk.ui.AddEditNote
import com.example.notetalk.ui.DeleteConfirmationAlertDialog
import com.example.notetalk.ui.MainViewModel
import com.example.notetalk.ui.NoteDetail
import com.example.notetalk.ui.NoteViewModel
import com.example.notetalk.ui.SettingsScreen
import com.example.notetalk.ui.theme.NoteTalkTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
/*
        // Get the database and DAO as before
        val database = NoteDatabase.getDatabase(applicationContext)
        val dao = database.noteDao()
        // Create the repository instance
        val repository = NoteRepository(dao)

        // Create the ViewModel using the factory with the repository
        val viewModel: NoteViewModel by viewModels {
            NoteViewModelFactory(repository)
        }*/
         setContent {

             val mainViewModel: MainViewModel = hiltViewModel()
             val currentTheme by mainViewModel.theme.collectAsStateWithLifecycle(initialValue = Theme.SYSTEM)

             NoteTalkTheme(
                 darkTheme = when (currentTheme) {
                     Theme.LIGHT -> false
                     Theme.DARK -> true
                     else -> isSystemInDarkTheme()
                 }
             ) {
                NotesApp()
            }
        }
    }
}

@Composable
fun NotesApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "notesList") {
        composable("notesList") {
            val viewModel: NoteViewModel = hiltViewModel()
            NotesListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("addEditNote/{noteId}") { navBackStackEntry ->
            val viewModel: NoteViewModel = hiltViewModel()
            val noteId = navBackStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: 0
            AddEditNote(
                viewModel = viewModel,
                navController = navController,
                noteId = noteId
            )
        }

        composable("viewNote/{noteId}") { navBackStackEntry ->
            val viewModel: NoteViewModel = hiltViewModel()
            val noteId = navBackStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: 0
            NoteDetail( // in NoteViewScreen
                viewModel = viewModel,
                navController = navController,
                noteId = noteId
            )
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(viewModel: NoteViewModel, navController: NavHostController) {
//    val notes = viewModel.allNotes.collectAsState()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val noteToDelete = remember { mutableStateOf<Note?>(null) }

    noteToDelete.value?.let { note ->
        DeleteConfirmationAlertDialog(note, noteToDelete,viewModel)
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text("MY NOTES", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            actions = {
                IconButton(onClick = { navController.navigate("settings")}) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )
                 },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addEditNote/0") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clickable (
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null // disable the ripple effect on click
                ) {
                    focusManager.clearFocus() // hide the keyboard when clicking outside
                }
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                label = { Text("Search Notes") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 8.dp),
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                items(notes) { note ->
                    NoteItem(note = note,
                        onNoteClick = { navController.navigate("viewNote/${note.id}") },
                        onEditClick = { navController.navigate("addEditNote/${note.id}") },
                        onDeleteClick = { noteToDelete.value = note }
                    )
                }
            }
        }
    }
}

private fun formatDate(lastModified: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(lastModified))
}

@Composable
fun NoteItem(note: Note, onNoteClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onNoteClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(text = note.content,
                    maxLines = 2,
                    fontSize = 14.sp
                )
                Text(
                    text = formatDate(note.lastModified),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            IconButton(onClick = onEditClick ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Note")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Note")
            }
        }
    }
}

@Preview(name = "Note Item")
@Composable
fun NoteItemPreview() {
    val note = Note(
        id = 1,
        title = "BADAL",
        content = "This is random text"
    )
    NoteItem(note, {}, {}, {})
}