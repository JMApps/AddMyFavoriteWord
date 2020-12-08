package jmapps.addmyfavoriteword.data.database.room.dictionary.words

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordItemsDao {
    @Query("SELECT * FROM Table_of_words WHERE _id = :wordCategoryId ORDER BY CASE :orderBy WHEN 'addDateTime' THEN addDateTime WHEN 'changeDateTime' THEN changeDateTime WHEN 'color' THEN wordItemColor WHEN 'alphabet' THEN word END ASC")
    fun getAllWordsList(wordCategoryId: Long, orderBy: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWordItem(wordItems: WordItems)

    @Query("UPDATE Table_of_words SET word = :newWord, wordTranscription = :newWordTranscription, wordTranslate = :newWordTranslate, changeDateTime = :newDateTime WHERE _id = :wordId")
    fun updateWordItem(newWord: String, newWordTranscription: String, newWordTranslate: String, newDateTime: String, wordId: Long)

    @Query("DELETE FROM Table_of_words WHERE _id = :wordId")
    fun deleteWordItem(wordId: Long)

    @Query("DELETE FROM TABLE_OF_WORDS WHERE displayBy = :wordCategoryId")
    fun deleteAllWordFromCategory(wordCategoryId: Long)
}