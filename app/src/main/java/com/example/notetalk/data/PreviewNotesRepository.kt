package com.example.notetalk.data
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.notetalk.NotesListScreen
import com.example.notetalk.ui.NoteViewModel
import com.example.notetalk.ui.theme.NoteTalkTheme// <-- Use your actual theme name
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * A fake repository that provides a hardcoded list of notes for the preview.
 */
class PreviewNotesRepository : INoteRepository {

    // A static list of 4 notes for our preview.
    private val notes = flowOf(
        listOf(
            Note(
                id = 1,
                title = "Team Meeting Recap",
                content = "Discussed Q3 goals and project timelines. Action items assigned.",
                lastModified = 1721754000000L // Corresponds to Jul 23, 2024
            ),
            Note(
                id = 2,
                title = "Weekend Trip Ideas",
                content = "- Hiking in the nearby hills\n- Visit the new art museum",
                lastModified = 1721667600000L // Corresponds to Jul 22, 2024
            ),
            Note(
                id = 3,
                title = "Shopping List",
                content = "Milk, eggs, bread, and coffee beans.",
                lastModified = 1721581200000L // Corresponds to Jul 21, 2024
            ),
            Note(
                id = 4,
                title = "Book Recommendations",
                content = "Read 'Atomic Habits' by James Clear.",
                lastModified = 1721494800000L // Corresponds to Jul 20, 2024
            )
        )
    )

    override val allNotes: Flow<List<Note>> = notes
    override fun searchNotes(query: String): Flow<List<Note>> = notes // Return all for preview
    override fun getNote(id: Int): Flow<Note?> {
        return allNotes.map { notes ->
            notes.find { it.id == id }
        }
    }
    override suspend fun insertNote(note: Note) { /* Do nothing for previews */ }
    override suspend fun delete(note: Note) { /* Do nothing for previews */ }
}


@Preview(showBackground = true)
@Composable
fun NotesListScreenPreview() {
    // 1. Create an instance of our fake repository
    val fakeRepository = remember { PreviewNotesRepository() }

    // 2. Create the ViewModel with the fake repository
    val viewModel = remember { NoteViewModel(fakeRepository) }

    // 3. Create a dummy NavController
    val navController = rememberNavController()

    NoteTalkTheme {
        // 4. Call the actual screen with the fake dependencies
        NotesListScreen(viewModel = viewModel, navController = navController)
    }
}