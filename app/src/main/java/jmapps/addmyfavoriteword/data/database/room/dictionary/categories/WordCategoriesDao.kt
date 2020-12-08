package jmapps.addmyfavoriteword.data.database.room.dictionary.categories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordCategoriesDao {
    @Query("SELECT * FROM Table_of_word_categories ORDER BY CASE :orderBy WHEN 'addDateTime' THEN addDateTime WHEN 'changeDateTie' THEN changeDateTime WHEN 'color' THEN wordCategoryColor WHEN 'alphabet' THEN wordCategoryTitle END ASC")
    fun getWordCategoriesList(orderBy: String): LiveData<MutableList<WordCategories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWordCategory(wordCategories: WordCategories)

    @Query("UPDATE Table_of_word_categories SET wordCategoryTitle = :newTitle, wordCategoryColor = :newColor, changeDateTime = :newDateTime WHERE _id = :wordCategoryId")
    fun updateWordCategory(newTitle: String, newColor: String, newDateTime: String, wordCategoryId: Long)

    @Query("UPDATE Table_of_words SET wordItemColor = :newColor WHERE _id = :wordCategoryId")
    fun updateWordItemColor(newColor: String, wordCategoryId: Long)

    @Query("DELETE FROM Table_of_word_categories WHERE _id = :wordCategoryId")
    fun deleteWordCategory(wordCategoryId: Long)

    @Query("DELETE FROM Table_of_words WHERE _id = :wordCategoryId")
    fun deleteWordItem(wordCategoryId: Long)

    @Query("DELETE FROM Table_of_word_categories")
    fun deleteAllWordCategories()

    @Query("DELETE FROM Table_of_words")
    fun deleteAllWordItems()
}