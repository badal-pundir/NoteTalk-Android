package com.example.notetalk.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface INoteRepository {
    val allNotes: Flow<List<Note>>
    fun getNote(id: Int): Flow<Note?>
    suspend fun insertNote(note: Note)
    suspend fun delete(note: Note)
}
@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): INoteRepository {

    override val allNotes = noteDao.getAllNotes()

    override fun getNote(id: Int): Flow<Note?> = noteDao.getNoteById(id)

    override suspend fun insertNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.insertNote(note.copy(timestamp = System.currentTimeMillis()))
        }
    }

    override suspend fun delete(note: Note) {
        withContext(ioDispatcher) {
            noteDao.deleteNote(note)
        }
    }
}