package jmapps.addmyfavoriteword.data.database.room.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [NoteItems::class], version = 1, exportSchema = false)
abstract class NoteDataBaseHelper : RoomDatabase() {

    abstract fun noteItemsDao(): NoteItemsDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDataBaseHelper? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDataBaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDataBaseHelper::class.java,
                    "NoteDatabase")
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                INSTANCE.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database?.noteItemsDao()?.let { notes ->
                            populateItems(notes)
                        }
                    }
                }
            }
        }

        fun populateItems(noteItemsDao: NoteItemsDao) {
            noteItemsDao.getNotesList("addDateTime")
        }
    }
}