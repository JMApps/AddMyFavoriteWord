package jmapps.addmyfavoriteword.data.database.room.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table_of_task_categories")
data class TaskCategories(
    @PrimaryKey(autoGenerate = true) val _id: Long = 1,
    val title: String,
    val categoryColor: String,
    val addDateTime: String,
    val changeDateTime: String,
)