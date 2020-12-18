package jmapps.addmyfavoriteword.presentation.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.notes.NoteDataBaseHelper
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.presentation.ui.repositories.NoteItemsRepository
import kotlinx.coroutines.launch

class NotesItemViewModel(application: Application) : AndroidViewModel(application) {
    private val noteItemsRepository: NoteItemsRepository

    init {
        val noteItemsDao = NoteDataBaseHelper.getDatabase(application, viewModelScope).noteItemsDao()
        noteItemsRepository = NoteItemsRepository(noteItemsDao)
    }

    fun allNoteItems(orderBy: String): LiveData<MutableList<NoteItems>> = noteItemsRepository.allNoteItems(orderBy)

    fun insertNoteItem(noteItems: NoteItems) = viewModelScope.launch {
        noteItemsRepository.insertNoteItem(noteItems)
    }

    fun updateNoteItem(newNoteTitle: String, newNoteContent: String, newNoteColor: String, newPriority: Long, noteId: Long) = viewModelScope.launch {
        noteItemsRepository.updateNoteItem(newNoteTitle, newNoteContent, newNoteColor, newPriority, noteId)
    }

    fun deleteNoteItem(noteId: Long) = viewModelScope.launch {
        noteItemsRepository.deleteNoteItem(noteId)
    }

    fun deleteAllNoteItems() = viewModelScope.launch {
        noteItemsRepository.deleteAllNoteItems()
    }
}