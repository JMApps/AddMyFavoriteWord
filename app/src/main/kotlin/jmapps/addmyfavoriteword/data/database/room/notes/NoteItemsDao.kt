package jmapps.addmyfavoriteword.data.database.room.notes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteItemsDao {
    @Query("SELECT * FROM Table_of_notes ORDER BY CASE :orderBy WHEN 'addDateTime' THEN addDateTime WHEN 'changeDateTime' THEN changeDateTime WHEN 'color' THEN noteColor WHEN 'alphabet' THEN noteTitle WHEN 'alphabet' THEN noteContent END ASC, CASE :orderBy WHEN 'priority' THEN priority END DESC")
    fun getNotesList(orderBy: String): LiveData<MutableList<NoteItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteItem(noteItems: NoteItems?)

    @Query("UPDATE Table_of_notes SET noteTitle = :newNoteTitle, noteContent = :newNoteContent, noteColor = :newNoteColor, priority = :newPriority WHERE _id = :noteId")
    suspend fun updateNoteItem(newNoteTitle: String, newNoteContent: String, newNoteColor: String, newPriority: Long, noteId: Long)

    @Query("DELETE FROM Table_of_notes WHERE _id = :noteId")
    suspend fun deleteNoteItem(noteId: Long)

    @Query("DELETE FROM Table_of_notes")
    suspend fun deleteAllNoteItems()
}