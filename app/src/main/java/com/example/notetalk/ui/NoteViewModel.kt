package com.example.notetalk.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notetalk.data.INoteRepository
import com.example.notetalk.data.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: INoteRepository): ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery:StateFlow<String> = _searchQuery.asStateFlow()


   /* val allNotes: StateFlow<List<Note>> = noteDao.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )*/
   @OptIn(ExperimentalCoroutinesApi::class)
   val notes: StateFlow<List<Note>> = _searchQuery
       .flatMapLatest { query ->
        if (query.isBlank()) {  // if search is empty show whole list of notes
            noteRepository.allNotes
        } else {    // search fro notes that match the query
            noteRepository.searchNotes(query)
        }
    }
        .stateIn( // converts a Flow into a StateFlow, so we can collect it in our UI using collectAsState()
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    /*
      StateFlow for global UI states (note list, auth, theme, etc.)
      Flow<T> + collectAsStateWithLifecycle() for on-demand data (getNote(id))
     */
    fun getNote(id: Int): Flow<Note?> = noteRepository.getNote(id)
        /*.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )*/
    /*
    fun getNote(id: Int): StateFlow<Note?> = noteDao.getNoteById(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
     */
    /*fun addNote(note: Note) {
        viewModelScope.launch {
            noteDao.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }*/

    fun addNote(note: Note) {
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
        }
    }


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
/*

class NoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
