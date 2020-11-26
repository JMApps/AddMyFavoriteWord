package jmapps.addmyfavoriteword.data.database.room.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table_of_notes")
data class NoteItems(
    @PrimaryKey(autoGenerate = true) val _id: Long,
    val title: String,
    val content: String,
    val noteColor: String,
    val addDateTime: String,
    val changeDateTime: String,
    val priority: Long
)