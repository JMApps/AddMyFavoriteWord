package jmapps.addmyfavoriteword.presentation.ui.holders

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.WordItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class WordItemsHolder(wordView: View) : RecyclerView.ViewHolder(wordView),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private var sharedLocalPreferences: SharedLocalProperties

    val tvWordColor: TextView = wordView.findViewById(R.id.text_word_color)
    val tvWord: TextView = wordView.findViewById(R.id.text_word)
    val tvWordTranscription: TextView = wordView.findViewById(R.id.text_word_transcription)
    val tvWordTranslate: TextView = wordView.findViewById(R.id.text_word_translate)
    val tvWordPriority: TextView = wordView.findViewById(R.id.text_word_priority)
    val tvWordAddDateTime: TextView = wordView.findViewById(R.id.text_word_add_date_time)
    val tvWordChangeDateTime: TextView = wordView.findViewById(R.id.text_word_change_date_time)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
//        setTextSize()
//        setShowAddChangeDateTime()
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
//        setTextSize()
//        setShowAddChangeDateTime()
    }

//    private fun setTextSize() {
//        val textSize = sharedLocalPreferences.getIntValue(KEY_WORD_ITEM_TEXT_SIZE, 18)
//        tvWord.textSize = textSize!!.toFloat()
//        tvWordTranscription.textSize = textSize.toFloat()
//        tvWordTranslate.textSize = textSize.toFloat()
//    }
//
//    private fun setShowAddChangeDateTime() {
//        val addShowingDateTime = sharedLocalPreferences.getBooleanValue(KEY_WORD_ITEM_ADD_DATE_TIME, false)
//        val changeShowingDateTime = sharedLocalPreferences.getBooleanValue(KEY_WORD_ITEM_CHANGE_DATE_TIME, false)
//
//        if (!addShowingDateTime!!) {
//            tvWordAddDateTime.visibility = View.GONE
//        } else {
//            tvWordAddDateTime.visibility = View.VISIBLE
//        }
//
//        if (!changeShowingDateTime!!) {
//            tvWordChangeDateTime.visibility = View.GONE
//        } else {
//            tvWordChangeDateTime.visibility = View.VISIBLE
//        }
//    }
}