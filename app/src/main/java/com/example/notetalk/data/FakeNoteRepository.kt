package com.example.notetalk.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeNoteRepository : INoteRepository {
    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())

    override val allNotes: Flow<List<Note>> = notesFlow

    override fun getNote(id: Int): Flow<Note?> {
        return allNotes.map { notes -> notes.find { it.id == id } }
    }

    override suspend fun insertNote(note: Note) {

        val currentNotes = notesFlow.value.toMutableList()

        val existingIndex = currentNotes.indexOfFirst { it.id == note.id }

        if (existingIndex != -1) {
            currentNotes[existingIndex] = note
        } else {
            currentNotes.add(note)
        }

        notesFlow.value = currentNotes
    }

    override suspend fun delete(note: Note) {
        val currentNotes = notesFlow.value.toMutableList()
        currentNotes.remove(note)
        notesFlow.value = currentNotes
    }

}
