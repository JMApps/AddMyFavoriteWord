package jmapps.addmyfavoriteword.presentation.ui.repositories

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItemsDao

class TaskItemsRepository(private val taskItemsDao: TaskItemsDao) {
    fun allTaskItems(displayBy: Long, order: String): LiveData<MutableList<TaskItems>> {
        return taskItemsDao.getTaskItemsList(displayBy, order)
    }

    suspend fun insertTaskItem(taskItems: TaskItems) {
        taskItemsDao.insertTaskItem(taskItems)
    }

    suspend fun updateTaskItem(newTitle: String, displayBy: Long, newDateTime: String) {
        taskItemsDao.updateTaskItem(newTitle, displayBy, newDateTime)
    }

    suspend fun updateTaskTitle(newTitle: String, newDateTime: String, newPriority: Long, taskId: Long) {
        taskItemsDao.updateTaskTitle(newTitle, newDateTime, newPriority, taskId)
    }

    suspend fun updateState(newState: Boolean, newDateTime: String, id: Long) {
        taskItemsDao.updateState(newState,newDateTime,  id)
    }

    suspend fun deleteTaskItem(id: Long) {
        taskItemsDao.deleteTaskItem(id)
    }
    suspend fun deleteAllTaskFromCategory(taskCategoryId: Long) {
        taskItemsDao.deleteAllTaskFromCategory(taskCategoryId)
    }
}