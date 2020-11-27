package jmapps.addmyfavoriteword.data.database.room.tasks.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table_of_task_categories")
data class TaskCategories(
    @PrimaryKey(autoGenerate = true) val _id: Long,
    val taskCategoryTitle: String,
    val taskCategoryColor: String,
    val taskCategoryIntermediate: String,
    val addDateTime: String,
    val changeDateTime: String
)