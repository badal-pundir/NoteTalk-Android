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

    fun searchNotes(query:String): Flow<List<Note>>
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
            noteDao.insertNote(note.copy(lastModified = System.currentTimeMillis()))
        }
    }

    override suspend fun delete(note: Note) {
        withContext(ioDispatcher) {
            noteDao.deleteNote(note)
        }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query)
    }
}