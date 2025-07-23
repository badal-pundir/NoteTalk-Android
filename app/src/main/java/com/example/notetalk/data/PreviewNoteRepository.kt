package com.example.notetalk.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PreviewNoteRepository : INoteRepository {
    private val fakeNotes = listOf(
        Note(id = 1, title = "Design Meeting", content = "Discuss new UI mockups for the settings screen."),
        Note(id = 2, title = "Recipe", content = "Pasta with garlic, olive oil, and chili flakes.")
    )
    private val notesFlow = MutableStateFlow(fakeNotes)

    override val allNotes: Flow<List<Note>> = notesFlow
    override fun getNote(id: Int): Flow<Note?> = notesFlow.map { notes -> notes.find { it.id == id } }

    override suspend fun insertNote(note: Note) { /* Do nothing for preview */ }
    override suspend fun delete(note: Note) { /* Do nothing for preview */ }
    override fun searchNotes(query: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }
}
