package jmapps.addmyfavoriteword.presentation.ui.activities

import android.app.SearchManager
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityAddWordBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.OtherActivityPresenter
import jmapps.addmyfavoriteword.presentation.ui.adapters.WordItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddWordItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddWordItemBottomSheet.Companion.ARG_ADD_WORD_ITEM_BS
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.RenameWordItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.RenameWordItemBottomSheet.Companion.ARG_RENAME_WORD_ITEM_BS
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.ARG_TOOLS_WORD_ITEM_BS
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsWordItemBottomSheet.Companion.ARG_WORD_GRID_COUNT
import jmapps.addmyfavoriteword.presentation.ui.models.WordsItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.DeleteAlertUtil
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class AddWordActivity : AppCompatActivity(), ContractInterface.OtherView,
    DeleteAlertUtil.OnClickDelete, View.OnClickListener, SearchView.OnQueryTextListener,
    WordItemsAdapter.OnLongWordItemClick, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityAddWordBinding
    private lateinit var wordsItemViewModel: WordsItemViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherActivityPresenter: OtherActivityPresenter

    private var wordCategoryId: Long? = null
    private var wordCategoryTitle: String? = null
    private var wordCategoryColor: String? = null
    private var wordCategoryPriority: Long? = null
    private var defaultOrderIndex: Int? = null
    private var wordGridCount: Int? = null

    private lateinit var searchView: SearchView
    private lateinit var wordItemsAdapter: WordItemsAdapter

    private lateinit var deleteAlertDialog: DeleteAlertUtil

    companion object {
        const val KEY_WORD_CATEGORY_ID = "key_word_category_id"
        const val KEY_WORD_CATEGORY_TITLE = "key_word_category_title"
        const val KEY_WORD_CATEGORY_COLOR = "key_word_category_color"
        const val KEY_WORD_CATEGORY_PRIORITY = "key_word_category_priority"
        private const val KEY_ORDER_WORD_INDEX = "key_order_word_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        wordsItemViewModel = ViewModelProvider(this).get(WordsItemViewModel::class.java)
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

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
        defaultOrderIndex = sharedLocalPreferences.getIntValue(KEY_ORDER_WORD_INDEX, 0)
        wordGridCount = sharedLocalPreferences.getIntValue(ARG_WORD_GRID_COUNT, 2)

        otherActivityPresenter = OtherActivityPresenter(this)
        otherActivityPresenter.initView(wordCategoryId!!, defaultOrderIndex!!)

        deleteAlertDialog = DeleteAlertUtil(this, this)

        binding.addWordItemContent.rvWordItems.addOnScrollListener(onAddScroll)
        binding.addWordItemContent.fabAddWordItem.setOnClickListener(this)
    }

    override fun initView(displayBy: Long, orderBy: String) {
        wordsItemViewModel.getAllWordsList(displayBy, orderBy).observe(this, {
            it.let {
                val gridLayout = GridLayoutManager(this, wordGridCount!! + 1)
                binding.addWordItemContent.rvWordItems.layoutManager = gridLayout
                wordItemsAdapter = WordItemsAdapter(this, it, this)
                binding.addWordItemContent.rvWordItems.adapter = wordItemsAdapter
                otherActivityPresenter.updateState(it)
                otherActivityPresenter.defaultState()
            }
        })
    }

    override fun defaultState() {
        binding.addWordItemContent.rvWordItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.addWordItemContent.textMainWordDescription.visibility = otherActivityPresenter.descriptionMain()
        val categoryDescription = getString(R.string.description_add_first_word, "<b>$wordCategoryTitle</b>")
        binding.addWordItemContent.textMainWordDescription.text = Html.fromHtml(categoryDescription)
    }

    override fun updateState() {
        binding.addWordItemContent.rvWordItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.addWordItemContent.textMainWordDescription.visibility = otherActivityPresenter.descriptionMain()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_word_items, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_words).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_tools_word_items -> {
                val toolsWordItemBottomSheet = ToolsWordItemBottomSheet()
                toolsWordItemBottomSheet.show(supportFragmentManager, ARG_TOOLS_WORD_ITEM_BS)
            }
            R.id.item_order_by_add_time -> {
                changeOrderList(defaultOrderIndex = 0)
            }
            R.id.item_order_by_change_date_time -> {
                changeOrderList(defaultOrderIndex = 1)
            }
            R.id.item_order_by_alphabet -> {
                changeOrderList(defaultOrderIndex = 4)
            }
            R.id.action_delete_all_word_items -> {
                val deleteAllWordDescription = getString(R.string.dialog_message_are_sure_you_want_items_word, "<b>$wordCategoryTitle</b>")
                deleteAlertDialog.showAlertDialog(deleteAllWordDescription, 0, 0, getString(R.string.action_words_deleted))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            wordItemsAdapter.filter.filter("")
        } else {
            wordItemsAdapter.filter.filter(newText)
        }
        return true
    }

    override fun onClick(v: View?) {
        val addWordItem = AddWordItemBottomSheet.toInstance(wordCategoryId!!, wordCategoryColor!!, wordCategoryPriority!!)
        addWordItem.show(supportFragmentManager, ARG_ADD_WORD_ITEM_BS)
    }

    override fun itemClickRenameItem(wordItemId: Long, word: String, wordTranscription: String, wordTranslate: String) {
        val renameWordItem = RenameWordItemBottomSheet.toInstance(wordItemId, word, wordTranscription, wordTranslate)
        renameWordItem.show(supportFragmentManager, ARG_RENAME_WORD_ITEM_BS)
    }

    override fun itemClickDeleteItem(wordItemId: Long) {
        deleteAlertDialog.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_item_word), 1, wordItemId, getString(R.string.action_word_deleted))
    }

    override fun onClickDeleteOnly(_id: Long) {
        wordsItemViewModel.deleteWordItem(_id)
    }

    override fun onClickDeleteAll() {
        wordsItemViewModel.deleteAllWordFromCategory(wordCategoryId!!)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        wordGridCount = sharedPreferences?.getInt(ARG_WORD_GRID_COUNT, 2)
        otherActivityPresenter.initView(wordCategoryId!!, defaultOrderIndex!!)
    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.addWordItemContent.fabAddWordItem.hide()
            } else {
                binding.addWordItemContent.fabAddWordItem.show()
            }
        }
    }

    private fun changeOrderList(defaultOrderIndex: Int) {
        sharedLocalPreferences.saveIntValue(KEY_ORDER_WORD_INDEX, defaultOrderIndex)
        otherActivityPresenter.initView(wordCategoryId!!, defaultOrderIndex)
        recreate()
    }
}