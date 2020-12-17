package jmapps.addmyfavoriteword.data.database.room.dictionary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jmapps.addmyfavoriteword.data.database.room.dictionary.categories.WordCategories
import jmapps.addmyfavoriteword.data.database.room.dictionary.categories.WordCategoriesDao
import jmapps.addmyfavoriteword.data.database.room.dictionary.words.WordItems
import jmapps.addmyfavoriteword.data.database.room.dictionary.words.WordItemsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [WordCategories::class, WordItems::class], version = 5, exportSchema = false)
abstract class WordDataBaseHelper : RoomDatabase() {

    abstract fun wordCategoriesDao(): WordCategoriesDao
    abstract fun wordItemsDao(): WordItemsDao

    companion object {

        @Volatile
        private var INSTANCE: WordDataBaseHelper? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE words_new (itemId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, word TEXT NOT NULL DEFAULT null, wordTranslate TEXT NOT NULL DEFAULT null, sampleBy INTEGER NOT NULL DEFAULT 0, addDateTime TEXT NOT NULL DEFAULT null, changeDateTime TEXT NOT NULL DEFAULT null)")
                database.execSQL("UPDATE Table_of_words SET addDateTime = 'null', changeDateTime = 'null' WHERE itemId")
                database.execSQL("INSERT INTO words_new (word, wordTranslate, addDateTime, changeDateTime) SELECT word, wordTranslate, addDateTime, changeDateTime FROM Table_of_words")
                database.execSQL("DROP TABLE Table_of_words")
                database.execSQL("ALTER TABLE words_new RENAME TO Table_of_words")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE Table_of_categories (categoryId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, categoryName TEXT NOT NULL DEFAULT null)")
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE words_new (wordId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, word TEXT NOT NULL DEFAULT null, wordTranslate TEXT NOT NULL DEFAULT null, displayBy INTEGER NOT NULL DEFAULT 0, addDateTime TEXT NOT NULL DEFAULT null, changeDateTime TEXT NOT NULL DEFAULT null)")
                database.execSQL("INSERT INTO words_new (word, wordTranslate, addDateTime, changeDateTime) SELECT word, wordTranslate, addDateTime, changeDateTime FROM Table_of_words")
                database.execSQL("DROP TABLE Table_of_words")
                database.execSQL("ALTER TABLE words_new RENAME TO Table_of_words")
                database.execSQL("UPDATE Table_of_words SET displayBy = 0 WHERE wordId")
                database.execSQL("INSERT INTO Table_of_categories (categoryId, categoryName) VALUES (0, 'Default')")
            }
        }

        private val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE word_categories_new (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, wordCategoryTitle TEXT NOT NULL DEFAULT null, wordCategoryColor TEXT NOT NULL DEFAULT null, priority INTEGER NOT NULL DEFAULT null, addDateTime TEXT NOT NULL DEFAULT null, changeDateTime TEXT NOT NULL DEFAULT null)")
                database.execSQL("INSERT INTO word_categories_new (_id, wordCategoryTitle) SELECT categoryId, categoryName FROM Table_of_categories")
                database.execSQL("DROP TABLE Table_of_categories")
                database.execSQL("ALTER TABLE words_categories_new RENAME TO Table_of_word_categories")
                database.execSQL("CREATE TABLE words_new (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, displayBy INTEGER NOT NULL DEFAULT null, word TEXT NOT NULL DEFAULT null, wordTranscription TEXT NOT NULL DEFAULT null, wordTranslate TEXT NOT NULL DEFAULT null, wordItemColor TEXT NOT NULL DEFAULT null, addDateTime TEXT NOT NULL DEFAULT null, changeDateTime TEXT NOT NULL DEFAULT null)")
                database.execSQL("INSERT INTO words_new (_id, displayBy, word, wordTranslate, addDateTime, changeDateTime) SELECT wordId, displayBy, word, wordTranslate, addDateTime, changeDateTime")
                database.execSQL("DROP TABLE Table_of_words")
                database.execSQL("ALTER TABLE words_new RENAME TO Table_of_words")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): WordDataBaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDataBaseHelper::class.java,
                    "WordsDatabase")
                    .addCallback(WordsDatabaseCallback(scope))
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
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
                        database?.wordCategoriesDao()?.let { populateWordCategories(it) }
                        database?.wordItemsDao()?.let { populateWordItems(it) }
                    }
                }
            }
        }

        fun populateWordCategories(wordCategoriesDao: WordCategoriesDao) {
            wordCategoriesDao.getWordCategoriesList("AddDateTime")
        }

        fun populateWordItems(wordItemsDao: WordItemsDao) {
            wordItemsDao.getAllWordsList(0, "AddDateTime")
        }
    }
}