package jmapps.addmyfavoriteword.data.database.room.tasks.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table_of_task_items")
data class TaskItems(
    @PrimaryKey(autoGenerate = true) val _id: Long,
    val taskItemTitle: String,
    val displayBy: Long,
    val taskItemColor: String,
    val addDateTime: String,
    val changeDateTime: String,
    val executionDateTime: String,
    val priority: Long,
    val currentTaskState: Boolean,
)