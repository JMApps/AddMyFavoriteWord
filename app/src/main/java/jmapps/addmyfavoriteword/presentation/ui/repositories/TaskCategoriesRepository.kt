package jmapps.addmyfavoriteword.presentation.ui.repositories

import androidx.lifecycle.LiveData
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategoriesDao

class TaskCategoriesRepository(private val taskCategoriesDao: TaskCategoriesDao) {
    fun allTaskCategories(order: String): LiveData<MutableList<TaskCategories>> {
        return taskCategoriesDao.getTaskCategoriesList(order)
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