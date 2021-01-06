package jmapps.addmyfavoriteword.presentation.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.dictionary.WordDataBaseHelper
import jmapps.addmyfavoriteword.data.database.room.dictionary.words.WordItems
import jmapps.addmyfavoriteword.presentation.ui.repositories.WordItemsRepository
import kotlinx.coroutines.launch

class WordsItemViewModel(application: Application) : AndroidViewModel(application) {
    private val wordItemsRepository: WordItemsRepository

    init {
        val wordItemsDao = WordDataBaseHelper.getDatabase(application, viewModelScope).wordItemsDao()
        wordItemsRepository = WordItemsRepository(wordItemsDao)
    }

    fun getAllWordsList(displayBy: Long, orderBy: String): LiveData<MutableList<WordItems>> = wordItemsRepository.getAllWordItems(displayBy, orderBy)

    fun insertWordItem(wordItems: WordItems) = viewModelScope.launch {
        wordItemsRepository.insertWordItem(wordItems)
    }

    fun updateWordItem(newWord: String, newWordTranscription: String, newWordTranslate: String, newDisplayBy: Long, newDateTime: String, wordId: Long) = viewModelScope.launch {
        wordItemsRepository.updateWordItem(newWord, newWordTranscription, newWordTranslate, newDisplayBy, newDateTime, wordId)
    }

    fun deleteWordItem(wordId: Long) = viewModelScope.launch {
        wordItemsRepository.deleteWordItem(wordId)
    }

    fun deleteAllWordFromCategory(wordCategoryId: Long) = viewModelScope.launch {
        wordItemsRepository.deleteAllWordFromCategory(wordCategoryId)
    }
}