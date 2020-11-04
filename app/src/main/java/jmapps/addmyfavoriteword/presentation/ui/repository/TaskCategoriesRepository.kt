package jmapps.addmyfavoriteword.presentation.ui.repository

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategories
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategoriesDao

class TaskCategoriesRepository(private val taskCategoriesDao: TaskCategoriesDao) {
    fun allTaskCategories(): LiveData<MutableList<TaskCategories>> {
        return taskCategoriesDao.getTaskCategoriesList()
    }

    suspend fun insertTaskCategory(taskCategories: TaskCategories) {
        taskCategoriesDao.insertTaskCategory(taskCategories)
    }

    suspend fun updateTaskCategory(newTitle: String, newCategoryColor: String, newDateTime: String) {
        taskCategoriesDao.updateTaskCategoryTitle(newTitle, newCategoryColor, newDateTime)
    }

    suspend fun deleteTaskCategory(id: Long) {
        taskCategoriesDao.deleteTaskCategory(id)
    }
}