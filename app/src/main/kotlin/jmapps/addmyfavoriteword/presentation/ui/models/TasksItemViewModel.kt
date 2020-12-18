package jmapps.addmyfavoriteword.presentation.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskDatabaseHelper
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.presentation.ui.repositories.TaskItemsRepository
import kotlinx.coroutines.launch

class TasksItemViewModel(application: Application) : AndroidViewModel(application) {
    private val taskItemsRepository: TaskItemsRepository

    init {
        val taskItemsDao = TaskDatabaseHelper.getDatabase(application, viewModelScope).taskItemsDao()
        taskItemsRepository = TaskItemsRepository(taskItemsDao)
    }

    fun allTaskItems(displayBy: Long, orderBy: String): LiveData<MutableList<TaskItems>> = taskItemsRepository.allTaskItems(displayBy, orderBy)

    fun insertTaskItem(taskItems: TaskItems) = viewModelScope.launch {
        taskItemsRepository.insertTaskItem(taskItems)
    }

    fun updateTaskItem(newTaskItemTitle: String, displayBy: Long, newDateTime: String) = viewModelScope.launch {
            taskItemsRepository.updateTaskItem(newTaskItemTitle, displayBy, newDateTime)
        }

    fun updateTaskTitle(newTaskItemTitle: String, newDateTime: String, newPriority: Long, taskItemId: Long) = viewModelScope.launch {
        taskItemsRepository.updateTaskTitle(newTaskItemTitle, newDateTime, newPriority, taskItemId)
    }

    fun updateState(newState: Boolean, newDateTime: String, taskItemId: Long) = viewModelScope.launch {
        taskItemsRepository.updateState(newState, newDateTime, taskItemId)
    }

    fun deleteTaskItem(taskItemId: Long) = viewModelScope.launch {
        taskItemsRepository.deleteTaskItem(taskItemId)
    }

    fun deleteAllTaskFromCategory(taskCategoryId: Long) = viewModelScope.launch {
        taskItemsRepository.deleteAllTaskFromCategory(taskCategoryId)
    }
}