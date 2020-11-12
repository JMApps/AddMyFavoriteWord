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

    suspend fun updateTask(newTitle: String, displayBy: Long, newDateTime: String) {
        taskItemsDao.updateTaskItem(newTitle, displayBy, newDateTime)
    }

    suspend fun updateState(newState: Boolean, id: Long, newDateTime: String) {
        taskItemsDao.updateState(newState, id, newDateTime)
    }

    suspend fun deleteTaskItem(id: Long) {
        taskItemsDao.deleteTaskItem(id)
    }
}