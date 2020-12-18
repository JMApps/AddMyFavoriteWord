package jmapps.addmyfavoriteword.presentation.ui.repositories

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItemsDao

class TaskItemsRepository(private val taskItemsDao: TaskItemsDao) {
    fun allTaskItems(displayBy: Long, orderBy: String): LiveData<MutableList<TaskItems>> {
        return taskItemsDao.getTaskItemsList(displayBy, orderBy)
    }

    suspend fun insertTaskItem(taskItems: TaskItems) {
        taskItemsDao.insertTaskItem(taskItems)
    }

    suspend fun updateTaskItem(newTaskItemTitle: String, displayBy: Long, newDateTime: String) {
        taskItemsDao.updateTaskItem(newTaskItemTitle, displayBy, newDateTime)
    }

    suspend fun updateTaskTitle(newTaskItemTitle: String, newDateTime: String, newPriority: Long, taskItemId: Long) {
        taskItemsDao.updateTaskTitle(newTaskItemTitle, newDateTime, newPriority, taskItemId)
    }

    suspend fun updateState(newState: Boolean, newDateTime: String, taskItemId: Long) {
        taskItemsDao.updateState(newState, newDateTime, taskItemId)
    }

    suspend fun deleteTaskItem(taskItemId: Long) {
        taskItemsDao.deleteTaskItem(taskItemId)
    }

    suspend fun deleteAllTaskFromCategory(taskCategoryId: Long) {
        taskItemsDao.deleteAllTaskFromCategory(taskCategoryId)
    }
}