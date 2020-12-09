package jmapps.addmyfavoriteword.presentation.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityAddWordBinding

class AddWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddWordBinding

    private var wordCategoryId: Long? = null
    private var wordCategoryTitle: String? = null
    private var wordCategoryColor: String? = null
    private var wordCategoryPriority: Long? = null
    private var defaultOrderIndex: Int? = null

    companion object {
        const val KEY_WORD_CATEGORY_ID = "key_word_category_id"
        const val KEY_WORD_CATEGORY_TITLE = "key_word_category_title"
        const val KEY_WORD_CATEGORY_COLOR = "key_word_category_color"
        const val KEY_WORD_CATEGORY_PRIORITY = "key_word_category_priority"
        private const val KEY_ORDER_WORD_INDEX = "key_order_word_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_word)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        wordCategoryId = intent.getLongExtra(KEY_WORD_CATEGORY_ID, 0)
        wordCategoryTitle = intent.getStringExtra(KEY_WORD_CATEGORY_TITLE)
        wordCategoryColor = intent.getStringExtra(KEY_WORD_CATEGORY_COLOR)
        wordCategoryPriority = intent.getLongExtra(KEY_WORD_CATEGORY_PRIORITY, 0)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = wordCategoryTitle
        }
    }
}