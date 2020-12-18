package jmapps.addmyfavoriteword.presentation.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskDatabaseHelper
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.presentation.ui.repositories.TaskCategoriesRepository
import kotlinx.coroutines.launch

class TasksCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val taskCategoryRepository: TaskCategoriesRepository

    init {
        val taskCategoriesDao = TaskDatabaseHelper.getDatabase(application, viewModelScope).taskCategoriesDao()
        taskCategoryRepository = TaskCategoriesRepository(taskCategoriesDao)
    }

    fun allTaskCategories(orderBy: String): LiveData<MutableList<TaskCategories>> = taskCategoryRepository.allTaskCategories(orderBy)

    fun insertTaskCategory(taskCategories: TaskCategories) = viewModelScope.launch {
        taskCategoryRepository.insertTaskCategory(taskCategories)
    }

    fun updateTaskCategory(newTaskCategoryTitle: String, newTaskCategoryColor: String, newDateTime: String, taskCategoryId: Long) = viewModelScope.launch {
        taskCategoryRepository.updateTaskCategory(newTaskCategoryTitle, newTaskCategoryColor, newDateTime, taskCategoryId)
    }

    fun updateTaskItemColor(newTaskCategoryColor: String, taskCategoryId: Long) = viewModelScope.launch {
        taskCategoryRepository.updateTaskItemColor(newTaskCategoryColor, taskCategoryId)
    }

    fun deleteTaskCategory(taskCategoryId: Long) = viewModelScope.launch {
        taskCategoryRepository.deleteTaskCategory(taskCategoryId)
    }

    fun deleteTaskItem(taskCategoryId: Long) = viewModelScope.launch {
        taskCategoryRepository.deleteTaskItem(taskCategoryId)
    }

    fun deleteAllTaskCategories() = viewModelScope.launch {
        taskCategoryRepository.deleteAllTaskCategories()
    }

    fun deleteAllTaskItem() = viewModelScope.launch {
        taskCategoryRepository.deleteAllTaskItems()
    }
}