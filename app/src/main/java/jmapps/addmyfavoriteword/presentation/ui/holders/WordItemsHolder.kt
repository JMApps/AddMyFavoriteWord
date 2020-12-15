package jmapps.addmyfavoriteword.presentation.ui.holders

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.WordItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.KEY_WORDS_TEXT_SIZE
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.KEY_WORD_STATE
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.KEY_WORD_TRANSCRIPTION_STATE
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.KEY_WORD_TRANSLATE_STATE
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class WordItemsHolder(wordView: View) : RecyclerView.ViewHolder(wordView),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private var sharedLocalPreferences: SharedLocalProperties

    val tvWordColor: TextView = wordView.findViewById(R.id.text_word_color)
    val tvWord: TextView = wordView.findViewById(R.id.text_word)
    private val tvWordTranscription: TextView = wordView.findViewById(R.id.text_word_transcription)
    val tvWordTranslate: TextView = wordView.findViewById(R.id.text_word_translate)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
        setTextSize()
        setShowWord()
    }

    fun findOnLongWordItemClick(onLongWordItemClick: WordItemsAdapter.OnLongWordItemClick, wordItemId: Long, word: String, wordTranscription: String, wordTranslate: String) {
        itemView.setOnClickListener {
            val pop = PopupMenu(itemView.context, tvWord)
            pop.inflate(R.menu.menu_change_item_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_change_item -> {
                        onLongWordItemClick.itemClickRenameItem(wordItemId, word, wordTranscription, wordTranslate)
                    }
                    R.id.popup_delete_item -> {
                        onLongWordItemClick.itemClickDeleteItem(wordItemId)
                    }
                }
                true
            }
            pop.show()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setTextSize()
        setShowWord()
    }

    private fun setTextSize() {
        val textSize = sharedLocalPreferences.getIntValue(KEY_WORDS_TEXT_SIZE, 18)
        tvWord.textSize = textSize!!.toFloat()
        tvWordTranscription.textSize = textSize.toFloat()
        tvWordTranslate.textSize = textSize.toFloat()
    }

    private fun setShowWord() {
        val wordState = sharedLocalPreferences.getBooleanValue(KEY_WORD_STATE, true)
        val wordTranslateState = sharedLocalPreferences.getBooleanValue(KEY_WORD_TRANSLATE_STATE, true)

        if (wordState!!) tvWord.visibility = View.VISIBLE else tvWord.visibility = View.GONE
        if (wordTranslateState!!) tvWordTranslate.visibility = View.VISIBLE else tvWordTranslate.visibility = View.GONE
    }

    // Доработать
    fun wordTranscriptionState(strWordTranscription: String) {
        val wordTranscriptionState = sharedLocalPreferences.getBooleanValue(KEY_WORD_TRANSCRIPTION_STATE, true)
        if (strWordTranscription.isNotEmpty()) {
            if (wordTranscriptionState!!) tvWordTranscription.visibility = View.VISIBLE else tvWordTranscription.visibility = View.GONE
            tvWordTranscription.text = strWordTranscription
        } else {
            tvWordTranscription.visibility = View.GONE
        }
    }
}