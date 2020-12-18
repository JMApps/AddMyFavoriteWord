package jmapps.addmyfavoriteword.data.database.room.tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategoriesDao
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItemsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TaskCategories::class, TaskItems::class], version = 1, exportSchema = false)
abstract class TaskDatabaseHelper : RoomDatabase() {

    abstract fun taskCategoriesDao(): TaskCategoriesDao
    abstract fun taskItemsDao(): TaskItemsDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabaseHelper? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TaskDatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabaseHelper::class.java,
                    "TaskDatabase")
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                INSTANCE.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database?.taskCategoriesDao()?.let { taskCategories ->
                            populateCategories(taskCategories)
                        }
                        database?.taskItemsDao()?.let { taskItems ->
                            populateItems(taskItems)
                        }
                    }
                }
            }
        }

        fun populateCategories(taskCategoriesDao: TaskCategoriesDao) {
            taskCategoriesDao.getTaskCategoriesList("addDateTime")
        }

        fun populateItems(taskItemsDao: TaskItemsDao) {
            taskItemsDao.getTaskItemsList(0, "addDateTime")
        }
    }
}