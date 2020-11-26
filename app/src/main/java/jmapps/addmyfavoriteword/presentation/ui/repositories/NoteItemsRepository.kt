package jmapps.addmyfavoriteword.presentation.ui.repositories

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItems
import jmapps.addmyfavoriteword.data.database.room.notes.NoteItemsDao

class NoteItemsRepository(private val noteItemsDao: NoteItemsDao) {
    fun allNoteItems(orderBy: String): LiveData<MutableList<NoteItems>> {
        return noteItemsDao.getNotesList(orderBy)
    }

    suspend fun insertNoteItem(noteItems: NoteItems) {
        noteItemsDao.insertNoteItem(noteItems)
    }

    suspend fun updateNoteItem(newTitle: String, newContent: String, newNoteColor: String, newPriority: Long, noteId: Long) {
        noteItemsDao.updateNoteItem(newTitle, newContent, newNoteColor, newPriority, noteId)
    }

    suspend fun deleteNoteItem(noteId: Long) {
        noteItemsDao.deleteNoteItem(noteId)
    }

    suspend fun deleteAllNoteItems() {
        noteItemsDao.deleteAllNoteItems()
    }
}