package jmapps.addmyfavoriteword.data.database.room.tasks.tasks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskItemsDao {
    @Query("SELECT * FROM Table_of_task_items WHERE displayBy = :displayBy ORDER BY CASE :orderBy WHEN 'addDateTime' THEN addDateTime END ASC, CASE :orderBy WHEN 'executionDateTime' THEN executionDateTime END DESC, CASE :orderBy WHEN 'alphabet' THEN taskItemTitle END ASC, CASE :orderBy WHEN 'priority' THEN priority END DESC")
    fun getTaskItemsList(displayBy: Long, orderBy: String): LiveData<MutableList<TaskItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItems: TaskItems?)

    @Query("UPDATE Table_of_task_items SET taskItemTitle = :newTaskItemTitle, displayBy = :displayBy, changeDateTime = :newDateTime")
    suspend fun updateTaskItem(newTaskItemTitle: String, displayBy: Long, newDateTime: String)

    @Query("UPDATE Table_of_task_items SET taskItemTitle = :newTaskItemTitle, changeDateTime = :newDateTime, priority = :newPriority WHERE _id = :taskItemId")
    suspend fun updateTaskTitle(newTaskItemTitle: String, newDateTime: String, newPriority: Long, taskItemId: Long)

    @Query("UPDATE Table_of_task_items SET currentTaskState = :newState, executionDateTime = :newDateTime WHERE _id = :taskItemId")
    suspend fun updateState(newState: Boolean, newDateTime: String, taskItemId: Long)

    @Query("DELETE FROM Table_of_task_items WHERE _id = :taskItemId")
    suspend fun deleteTaskItem(taskItemId: Long)

    @Query("DELETE FROM Table_of_task_items WHERE displayBy = :taskCategoryId")
    suspend fun deleteAllTaskFromCategory(taskCategoryId: Long)
}