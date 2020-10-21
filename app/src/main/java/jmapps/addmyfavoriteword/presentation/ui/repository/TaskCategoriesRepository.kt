package jmapps.addmyfavoriteword.presentation.ui.repository

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategories
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategoriesDao

class TaskCategoriesRepository(private val taskCategoriesDao: TaskCategoriesDao) {
    val allTaskCategories: LiveData<MutableList<TaskCategories>> = taskCategoriesDao.getTaskCategoriesList()

    suspend fun insertTaskCategory(taskCategories: TaskCategories) {
        taskCategoriesDao.insertTaskCategory(taskCategories)
    }

    suspend fun updateTaskCategory(newTitle: String) {
        taskCategoriesDao.updateTaskCategoryTitle(newTitle)
    }

    suspend fun deleteTaskCategory(id: Long) {
        taskCategoriesDao.deleteTaskCategory(id)
    }
}