package jmapps.addmyfavoriteword.data.database.room.tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TaskCategories::class], version = 1, exportSchema = false)
abstract class TaskDatabaseHelper : RoomDatabase() {

    abstract fun taskCategoriesDao(): TaskCategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabaseHelper? = null

        fun getDatabase(context: Context, scope: CoroutineScope): TaskDatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabaseHelper::class.java,
                    "TaskDatabase"
                )
                    .addCallback(WordsDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class WordsDatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                INSTANCE.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database?.taskCategoriesDao()?.let { populateCategories(it) }
                    }
                }
            }
        }

        fun populateCategories(taskCategoriesDao: TaskCategoriesDao) {
            taskCategoriesDao.getTaskCategoriesList()
        }
    }
}