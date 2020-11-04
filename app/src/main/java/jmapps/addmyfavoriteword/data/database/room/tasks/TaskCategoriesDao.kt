package jmapps.addmyfavoriteword.data.database.room.tasks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskCategoriesDao {
    @Query("SELECT * FROM Table_of_task_categories ORDER BY addDateTime ASC")
    fun getTaskCategoriesList(): LiveData<MutableList<TaskCategories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategory(taskCategories: TaskCategories?)

    @Query("UPDATE Table_of_task_categories SET title = :newTitle, categoryColor = :newCategoryColor, changeDateTime = :newDateTime")
    suspend fun updateTaskCategoryTitle(
        newTitle: String,
        newCategoryColor: String,
        newDateTime: String
    )

    @Query("DELETE FROM Table_of_task_categories WHERE _id = :id")
    suspend fun deleteTaskCategory(id: Long)
}