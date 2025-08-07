package com.example.notetalk.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notetalk.R
import com.example.notetalk.ui.theme.NoteTalkTheme // Replace with your app's theme

// A simple data class for our notes
data class Note(val id: Int, val title: String, val content: String)
/*

class Activity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteTalkTheme() { // Use your app's theme
                KeepHomeScreen()
            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepHomeScreen() {
    // Dummy data to populate the UI
    val notes = remember {
        listOf(
            Note(1, "Meeting Notes", "Discuss project milestones and upcoming deadlines. Q3 goals."),
            Note(2, "Shopping List", "Milk, Bread, Eggs, Cheese, Fruits"),
            Note(3, "Book Ideas", "A sci-fi novel about AI consciousness. A fantasy story set in an underwater world."),
            Note(4, "Workout Plan", "Monday: Chest & Triceps\nWednesday: Back & Biceps\nFriday: Legs & Shoulders"),
            Note(5, "Recipe: Pasta", "Ingredients: Pasta, tomatoes, garlic, olive oil, basil. Cook pasta. Sauté garlic. Add tomatoes. Mix and serve."),
            Note(6, "Personal Goals", "Read 12 books this year. Learn Jetpack Compose. Run a 5k."),
            Note(7, "Movies to Watch", "Dune: Part Two, The Creator, Oppenheimer"),
            Note(8, "Book Ideas", "A sci-fi novel about AI consciousness. A fantasy story set in an underwater world."),
            Note(9, "Workout Plan", "Monday: Chest & Triceps\nWednesday: Back & Biceps\nFriday: Legs & Shoulders"),
            Note(10, "Recipe: Pasta", "Ingredients: Pasta, tomatoes, garlic, olive oil, basil. Cook pasta. Sauté garlic. Add tomatoes. Mix and serve."),
            Note(11, "Personal Goals", "Read 12 books this year. Learn Jetpack Compose. Run a 5k."),
            Note(12, "Shopping List", "Milk, Bread, Eggs, Cheese, Fruits"),
            Note(13, "Movies to Watch", "Dune: Part Two, The Creator, Oppenheimer"),
            Note(14, "Meeting Notes", "Discuss project milestones and upcoming deadlines. Q3 goals.")
        )
    }

//    var contentListOrientation by remember { mutableStateOf(false) }
    var isGrid by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopSearchBarUI(isGrid, onToggleLayout = { isGrid = !isGrid}) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle new note creation */ },
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        NotesGrid(notes = notes, modifier = Modifier.padding(paddingValues), isGrid)
    }
}

@Composable
fun TopSearchBarUI(isGrid: Boolean, onToggleLayout: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },

            placeholder = { Text("Search your notes") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = {

                    IconButton(onClick = {
                        onToggleLayout()
                    }) {
                        Icon(
                            painter = if (isGrid) painterResource(R.drawable.view_stream_28dp_unfilled)
                            else painterResource(R.drawable.grid_view_28dp),
                            contentDescription = "Search Icon",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                    }
                    },
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            modifier = Modifier.weight(0.8f),
        )
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = {},
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .weight(0.1f)) {
            Icon(
                painter = painterResource(R.drawable.person_28dp),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
//                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun NotesGrid(notes: List<Note>, modifier: Modifier = Modifier, isGrid: Boolean) {
    AnimatedContent(
        targetState = isGrid,
        label = "GridToListTransition",
        transitionSpec = {
            // Defines the fade-in and fade-out animation
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        }
    ) { targetIsGrid -> // The content lambda gives you the target state
        if (targetIsGrid) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(notes, key = {it.id}) { note ->
                    NoteCard(note, Modifier.animateItemPlacement(
                        animationSpec = tween(500, delayMillis = 1000)
                    ))
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes, key = {it.id}) { note ->
                    NoteCard(note,
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(500, delayMillis = 1000)
                        ))
                }
            }
        }
    }
}
@Composable
fun NoteCard(note: Note, modifier: Modifier) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 10
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
     NoteTalkTheme{ // Use your app's theme
        KeepHomeScreen()
    }
}