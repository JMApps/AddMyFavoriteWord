package jmapps.addmyfavoriteword.presentation.ui.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import jmapps.addmyfavoriteword.data.database.room.dictionary.WordDataBaseHelper
import jmapps.addmyfavoriteword.data.database.room.dictionary.categories.WordCategories
import jmapps.addmyfavoriteword.presentation.ui.repositories.WordCategoriesRepository
import kotlinx.coroutines.launch

class WordsCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val wordCategoriesRepository: WordCategoriesRepository

    init {
        val wordCategoriesDao = WordDataBaseHelper.getDatabase(application, viewModelScope).wordCategoriesDao()
        wordCategoriesRepository = WordCategoriesRepository(wordCategoriesDao)
    }

    fun allWordCategories(orderBy: String): LiveData<MutableList<WordCategories>> = wordCategoriesRepository.allWordCategories(orderBy)

    fun insertWordCategory(wordCategories: WordCategories) = viewModelScope.launch {
        wordCategoriesRepository.insertWordCategory(wordCategories)
    }

    fun updateWordCategory(newTitle: String, newColor: String, newDateTime: String, newPriority: Long, wordCategoryId: Long) = viewModelScope.launch {
        wordCategoriesRepository.updateWordCategory(newTitle, newColor, newDateTime, newPriority, wordCategoryId)
    }

    fun updateWordItemColor(newColor: String, wordCategoryId: Long) = viewModelScope.launch {
        wordCategoriesRepository.updateWordItemColor(newColor, wordCategoryId)
    }

    fun deleteWordCategory(wordCategoryId: Long) = viewModelScope.launch {
        wordCategoriesRepository.deleteWordCategory(wordCategoryId)
    }

    fun deleteWordItem(wordCategoryId: Long) = viewModelScope.launch {
        wordCategoriesRepository.deleteWordItem(wordCategoryId)
    }

    fun deleteAllWordCategories() = viewModelScope.launch {
        wordCategoriesRepository.deleteAllWordCategories()
    }

    fun deleteAllWordItems() = viewModelScope.launch {
        wordCategoriesRepository.deleteAllWordItems()
    }
}