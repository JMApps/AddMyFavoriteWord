package jmapps.addmyfavoriteword.presentation.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategories
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskDatabaseHelper
import jmapps.addmyfavoriteword.presentation.ui.repository.TaskCategoriesRepository
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    private val taskCategoryRepository: TaskCategoriesRepository
    val allTaskCategories: LiveData<MutableList<TaskCategories>>

    init {
        val taskCategoriesDao = TaskDatabaseHelper.getDatabase(application, viewModelScope).taskCategoriesDao()
        taskCategoryRepository = TaskCategoriesRepository(taskCategoriesDao)
        allTaskCategories = taskCategoryRepository.allTaskCategories
    }

    fun insertTaskCategory(taskCategories: TaskCategories) = viewModelScope.launch {
        taskCategoryRepository.insertTaskCategory(taskCategories)
    }

    fun updateTaskCategory(newTitle: String, newDateTime: String) = viewModelScope.launch {
        taskCategoryRepository.updateTaskCategory(newTitle, newDateTime)
    }

    fun deleteTaskCategory(id: Long) = viewModelScope.launch {
        taskCategoryRepository.deleteTaskCategory(id)
    }
}