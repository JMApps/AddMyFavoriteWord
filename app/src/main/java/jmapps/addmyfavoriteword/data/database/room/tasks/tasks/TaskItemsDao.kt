package jmapps.addmyfavoriteword.data.database.room.tasks.tasks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskItemsDao {
    @Query("SELECT * FROM Table_of_task_items WHERE displayBy = :displayBy ORDER BY CASE :order WHEN 'addDateTime' THEN addDateTime WHEN 'executionDateTime' THEN executionDateTime WHEN 'alphabet' THEN title END ASC")
    fun getTaskItemsList(displayBy: Long, order: String): LiveData<MutableList<TaskItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItems: TaskItems?)

    @Query("UPDATE Table_of_task_items SET title = :newTitle, displayBy = :displayBy, changeDateTime = :newDateTime")
    suspend fun updateTaskItem(newTitle: String, displayBy: Long, newDateTime: String)

    @Query("UPDATE Table_of_task_items SET title = :newTitle, changeDateTime = :newDateTime")
    suspend fun updateTaskTitle(newTitle: String, newDateTime: String)

    @Query("UPDATE Table_of_task_items SET currentTaskState = :newState, executionDateTime = :newDateTime WHERE _id = :id")
    suspend fun updateState(newState: Boolean, id: Long, newDateTime: String)

    @Query("DELETE FROM Table_of_task_items WHERE _id = :id")
    suspend fun deleteTaskItem(id: Long)

    @Query("DELETE FROM Table_of_task_items WHERE displayBy = :taskCategoryId")
    suspend fun deleteAllTaskFromCategory(taskCategoryId: Long)
}