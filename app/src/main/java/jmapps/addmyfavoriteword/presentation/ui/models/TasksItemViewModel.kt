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

    fun updateTaskItem(newTitle: String, displayBy: Long, newDateTime: String) = viewModelScope.launch {
            taskItemsRepository.updateTaskItem(newTitle, displayBy, newDateTime)
        }

    fun updateTaskTitle(newTitle: String, newDateTime: String, taskId: Long) = viewModelScope.launch {
        taskItemsRepository.updateTaskTitle(newTitle, newDateTime, taskId)
    }

    fun updateState(newState: Boolean, id: Long, newDateTime: String) = viewModelScope.launch {
        taskItemsRepository.updateState(newState, id, newDateTime)
    }

    fun deleteTaskItem(id: Long) = viewModelScope.launch {
        taskItemsRepository.deleteTaskItem(id)
    }

    fun deleteAllTaskFromCategory(taskCategoryId: Long) = viewModelScope.launch {
        taskItemsRepository.deleteAllTaskFromCategory(taskCategoryId)
    }
}