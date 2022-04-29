package com.example.jetnote.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetnote.model.Note
import com.example.jetnote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    // var noteList = mutableStateListOf<Note>()

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.getAllNotes().distinctUntilChanged()
                .collect { notes ->
                    if (notes.isNullOrEmpty()) {
                        Log.d("RETRIVE NOTES", "empty list")
                    } else {
                        _noteList.value = notes
                    }
                }
        }
        // noteList.addAll(NoteData().loadNotes())
    }

    fun addNote(note: Note) = viewModelScope.launch { noteRepository.addNote(note) }
    fun removeNote(note: Note) = viewModelScope.launch { noteRepository.deleteNote(note) }
    fun updateNote(note: Note) = viewModelScope.launch { noteRepository.updateNote(note) }
    fun deleteAllNotes() = viewModelScope.launch { noteRepository.deleteAllNotes() }

    fun getAllNotes() = noteRepository.getAllNotes()
}
