package com.example.notetalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.data.Note
import com.example.notetalk.data.PreviewNoteRepository
import com.example.notetalk.data.Theme
import com.example.notetalk.ui.*
import com.example.notetalk.ui.theme.NoteTalkTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
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
    // Replacing NavHost with AnimatedNavHost for smooth screen transition.
    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable(
            route = "landing",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) {
            LandingScreen(onGetStartedClick = {
                navController.navigate("notesList") {
                    popUpTo("landing") { inclusive = true }
                }
            })
        }
        composable(
            route = "notesList",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
            ) {
            val viewModel: NoteViewModel = hiltViewModel()
            NotesListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            route = "addEditNote/{noteId}",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) { navBackStackEntry ->
            val viewModel: NoteViewModel = hiltViewModel()
            val noteId = navBackStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: 0
            AddEditNote(
                viewModel = viewModel,
                navController = navController,
                noteId = noteId
            )
        }

        composable(
            route = "viewNote/{noteId}",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) { navBackStackEntry ->
            val viewModel: NoteViewModel = hiltViewModel()
            val noteId = navBackStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: 0
            NoteDetail( // in NoteViewScreen
                viewModel = viewModel,
                navController = navController,
                noteId = noteId
            )
        }

        composable(route = "settings",
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
        ) {
            SettingsScreen(navController = navController)
        }
    }
}


@Composable
fun NotesListScreen(viewModel: NoteViewModel, navController: NavHostController) {
    // State from ViewModel
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Local UI state
    var isGrid by remember { mutableStateOf(true) }
    val noteToDelete = remember { mutableStateOf<Note?>(null) }
    var isSearchBarFocused by remember { mutableStateOf(false) }

    // Show confirmation dialog when a note is marked for deletion
    noteToDelete.value?.let { note ->
        DeleteConfirmationAlertDialog(
            note = note,
            noteToDelete = noteToDelete,
            viewModel = viewModel
        )
    }

    Scaffold(
        topBar = {
            TopSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                isGrid = isGrid,
                onToggleLayout = { isGrid = !isGrid },
                isFocused = isSearchBarFocused,
                onSearchBarFocusChanged = { isSearchBarFocused = it },
                onProfileClick = { navController.navigate("settings") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addEditNote/0") },
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        NotesGrid(
            notes = notes,
            isGrid = isGrid,
            modifier = Modifier.padding(paddingValues),
            onNoteClick = { noteId -> navController.navigate("viewNote/$noteId") },
            onEditClick = { noteId -> navController.navigate("addEditNote/$noteId") },
            onDeleteClick = { note -> noteToDelete.value = note }
        )
    }
}

@Composable
fun TopSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isGrid: Boolean,
    onToggleLayout:() -> Unit,
    isFocused: Boolean,
    onSearchBarFocusChanged: (Boolean) -> Unit,
    onProfileClick:() -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search your notes") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = {
            if(isFocused && searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChanged("") } ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear Search"
                    )
                }
            }
            else {
                IconButton(onClick = onToggleLayout) {
                    Icon(
                        painter = if (isGrid) painterResource(R.drawable.view_stream_28dp_unfilled)
                            else painterResource(R.drawable.grid_view_28dp),
                            contentDescription = "Toggle Layout"
                        )
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    onSearchBarFocusChanged(focusState.isFocused)
                },
        )
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onProfileClick) {
//            Icon(
//                painter = painterResource(R.drawable.person_28dp),
//                contentDescription = "Profile/Settings"
//            )
            ProfileIcon(
                imagePainter = painterResource(R.drawable.user1),
            )
        }
    }

}

//@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesGrid(
    notes: List<Note>,
    isGrid: Boolean,
    modifier: Modifier = Modifier,
    onNoteClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    AnimatedContent(
        targetState = isGrid,
        label = "GridToListTransition",
        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
    ) { targetIsGrid ->
        if (targetIsGrid) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
//                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onNoteClick(note.id) },
                        onEditClick = { onEditClick(note.id) },
                        onDeleteClick = { onDeleteClick(note) }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
//                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onNoteClick(note.id) },
                        onEditClick = { onEditClick(note.id) },
                        onDeleteClick = { onDeleteClick(note) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showMenu = true }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 10
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatDate(note.lastModified),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            // Dropdown menu for edit and delete
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        showMenu = false
                        onEditClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    }
                )
            }
        }
    }
}

// Helper function to format the timestamp
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun NotesListScreenPreview() {
    NoteTalkTheme {
        val fakeRepo = remember { PreviewNoteRepository() } // fake repo for preview
        val viewModel = remember { NoteViewModel(fakeRepo) }
        val navController = rememberNavController()
        NotesListScreen(viewModel = viewModel, navController = navController)
    }
}