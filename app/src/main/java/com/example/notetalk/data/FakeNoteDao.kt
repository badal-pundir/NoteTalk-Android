package com.example.notetalk.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// This Fake DAO now starts with a list of notes for previews.
class FakeNoteDao : NoteDao {

    // Use a MutableStateFlow to hold an in-memory list of notes.
    // It's initialized with our fake data.
    private val notesFlow = MutableStateFlow(getFakeNotes())

    override fun getAllNotes(): Flow<List<Note>> = notesFlow

    override fun getNoteById(id: Int): Flow<Note?> {
        return notesFlow.map { notes -> notes.find { it.id == id } }
    }

    override suspend fun insertNote(note: Note) {
        val currentNotes = notesFlow.value.toMutableList()
        val existingIndex = currentNotes.indexOfFirst { it.id == note.id && note.id != 0 }

        if (existingIndex != -1) {
            currentNotes[existingIndex] = note // Update
        } else {
            // For a more realistic insert, create a new ID if it's 0
            val newNote = if (note.id == 0) {
                note.copy(id = (currentNotes.maxOfOrNull { it.id } ?: 0) + 1)
            } else {
                note
            }
            currentNotes.add(newNote) // Insert
        }
        notesFlow.value = currentNotes
    }

    override suspend fun deleteNote(note: Note) {
        val currentNotes = notesFlow.value.toMutableList()
        currentNotes.remove(note)
        notesFlow.value = currentNotes
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    // Companion object to hold the fake data generation function.
    companion object {
        private fun getFakeNotes(): List<Note> {
            return listOf(
                Note(
                    id = 1,
                    title = "Meeting Prep",
                    content = "Review the quarterly report and prepare slides for the presentation on Friday."
                ),
                Note(
                    id = 2,
                    title = "Grocery List",
                    content = "- Milk\n- Bread\n- Eggs\n- Cheese"
                ),
                Note(
                    id = 3,
                    title = "Project Idea",
                    content = "Develop a note-taking app using Jetpack Compose and Room. Focus on clean architecture and a simple, intuitive UI."
                )
            )
        }
    }
}