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

    suspend fun updateTaskCategory(newTitle: String, newCategoryColor: String, newDateTime: String, id: Long) {
        taskCategoriesDao.updateTaskCategoryTitle(newTitle, newCategoryColor, newDateTime, id)
    }

    suspend fun deleteTaskCategory(id: Long) {
        taskCategoriesDao.deleteTaskCategory(id)
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