package jmapps.addmyfavoriteword.data.database.room.tasks.categories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskCategoriesDao {
    @Query("SELECT * FROM Table_of_task_categories ORDER BY CASE :orderBy WHEN 'addDateTime' THEN addDateTime END ASC, CASE :orderBy WHEN 'changeDateTime' THEN changeDateTime END DESC, CASE :orderBy WHEN 'color' THEN taskCategoryColor WHEN 'alphabet' THEN taskCategoryTitle END ASC")
    fun getTaskCategoriesList(orderBy: String): LiveData<MutableList<TaskCategories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCategory(taskCategories: TaskCategories?)

    @Query("UPDATE Table_of_task_categories SET taskCategoryTitle = :newTitle, taskCategoryColor = :newCategoryColor, changeDateTime = :newDateTime WHERE _id = :taskCategoryId")
    suspend fun updateTaskCategoryTitle(newTitle: String, newCategoryColor: String, newDateTime: String, taskCategoryId: Long)

    @Query("UPDATE Table_of_task_items SET taskItemColor = :newColor WHERE displayBy = :taskCategoryId")
    suspend fun updateTaskItemColor(newColor: String, taskCategoryId: Long)

    @Query("DELETE FROM Table_of_task_categories WHERE _id = :taskCategoryId")
    suspend fun deleteTaskCategory(taskCategoryId: Long)

    @Query("DELETE FROM Table_of_task_items WHERE displayBy = :taskCategoryId")
    suspend fun deleteTaskItem(taskCategoryId: Long)

    @Query("DELETE FROM Table_of_task_categories")
    suspend fun deleteAllTaskCategories()

    @Query("DELETE FROM Table_of_task_items")
    suspend fun deleteAllTaskItems()
}