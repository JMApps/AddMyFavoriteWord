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

    suspend fun updateTaskCategory(newTaskCategoryTitle: String, newTaskCategoryColor: String, newDateTime: String, taskCategoryId: Long) {
        taskCategoriesDao.updateTaskCategoryTitle(newTaskCategoryTitle, newTaskCategoryColor, newDateTime, taskCategoryId)
    }

    suspend fun updateTaskItemColor(newTaskCategoryTitle: String, taskCategoryId: Long) {
        taskCategoriesDao.updateTaskItemColor(newTaskCategoryTitle, taskCategoryId)
    }

    suspend fun deleteTaskCategory(taskCategoryId: Long) {
        taskCategoriesDao.deleteTaskCategory(taskCategoryId)
    }

    suspend fun deleteTaskItem(taskCategoryId: Long) {
        taskCategoriesDao.deleteTaskItem(taskCategoryId)
    }

    suspend fun deleteAllTaskCategories() {
        taskCategoriesDao.deleteAllTaskCategories()
    }

    suspend fun deleteAllTaskItems() {
        taskCategoriesDao.deleteAllTaskItems()
    }
}