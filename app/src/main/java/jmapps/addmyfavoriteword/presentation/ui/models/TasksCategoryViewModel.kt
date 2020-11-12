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

    fun allTaskCategories(order: String): LiveData<MutableList<TaskCategories>> = taskCategoryRepository.allTaskCategories(order)

    fun insertTaskCategory(taskCategories: TaskCategories) = viewModelScope.launch {
        taskCategoryRepository.insertTaskCategory(taskCategories)
    }

    fun updateTaskCategory(newTitle: String, newCategoryColor: String, newDateTime: String) =
        viewModelScope.launch {
            taskCategoryRepository.updateTaskCategory(newTitle, newCategoryColor, newDateTime)
        }

    fun deleteTaskCategory(id: Long) = viewModelScope.launch {
        taskCategoryRepository.deleteTaskCategory(id)
    }
}