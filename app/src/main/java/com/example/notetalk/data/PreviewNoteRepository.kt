package com.example.notetalk.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PreviewNoteRepository : INoteRepository {
    private val notes = flowOf(
        listOf(
        Note(id = 1, title = "Design Meeting", content = "Discuss new UI mockups for the settings screen."),
        Note(id = 2, title = "Recipe", content = "Pasta with garlic, olive oil, and chili flakes.")
        )
    )
    override val allNotes: Flow<List<Note>> = notes
    override fun searchNotes(query: String): Flow<List<Note>> = notes
    override fun getNote(id: Int): Flow<Note?> = flowOf(null)
    override suspend fun insertNote(note: Note) {}
    override suspend fun delete(note: Note) {}
}
