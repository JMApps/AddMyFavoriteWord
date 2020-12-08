package jmapps.addmyfavoriteword.presentation.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentDictionaryCategoryBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter
import jmapps.addmyfavoriteword.presentation.ui.adapters.WordCategoriesAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddWordCategoryBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.models.WordsCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.DeleteAlertUtil
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class DictionaryFragment : Fragment(), ContractInterface.OtherView, SearchView.OnQueryTextListener,
    View.OnClickListener, WordCategoriesAdapter.OnItemClickWordCategory,
    WordCategoriesAdapter.OnLongClickWordCategory, DeleteAlertUtil.OnClickDelete {

    private lateinit var binding: FragmentDictionaryCategoryBinding
    private lateinit var wordCategoriesViewModel: WordsCategoryViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    private lateinit var searchView: SearchView
    private lateinit var wordCategoriesAdapter: WordCategoriesAdapter

    private var defaultOrderIndex = 0
    private lateinit var deleteAlertDialog: DeleteAlertUtil

    companion object {
        private const val KEY_ORDER_WORD_CATEGORY_INDEX = "key_order_word_category_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordCategoriesViewModel = ViewModelProvider(this).get(WordsCategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dictionary_category, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        retainInstance = true
        setHasOptionsMenu(true)

        deleteAlertDialog = DeleteAlertUtil(requireContext(), this)

        defaultOrderIndex = sharedLocalPreferences.getIntValue(KEY_ORDER_WORD_CATEGORY_INDEX, 0)!!

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.initView(defaultOrderIndex)

        binding.rvWordCategories.addOnScrollListener(onAddScroll)
        binding.fabAddWordCategory.setOnClickListener(this)

        return binding.root
    }

    override fun initView(orderBy: String) {
        wordCategoriesViewModel.allWordCategories(orderBy).observe(this, {
            it.let {
                val verticalLayout = LinearLayoutManager(requireContext())
                binding.rvWordCategories.layoutManager = verticalLayout
                wordCategoriesAdapter = WordCategoriesAdapter(requireContext(), it, this, this)
                binding.rvWordCategories.adapter = wordCategoriesAdapter
                otherFragmentsPresenter.updateState(it)
                otherFragmentsPresenter.defaultState()
            }
        })
    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddWordCategory.startAnimation(show)

        binding.rvWordCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainDictionaryContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvWordCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainDictionaryContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_word_categories, menu)
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_categories).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_tools_word_categories -> {
            }
            R.id.item_order_by_add_time -> {
                changeOrderList(defaultOrderIndex = 0)
            }
            R.id.item_order_by_change_time -> {
                changeOrderList(defaultOrderIndex = 1)
            }
            R.id.item_order_by_color -> {
                changeOrderList(defaultOrderIndex = 3)
            }
            R.id.item_order_by_alphabet -> {
                changeOrderList(defaultOrderIndex = 4)
            }
            R.id.action_delete_all_categories -> {
                deleteAlertDialog.showAlertDialog(getString(R.string.dialog_message_are_sure_you_want_word_categories), 0, 0, getString(R.string.action_categories_deleted))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            wordCategoriesAdapter.filter.filter("")
        } else {
            wordCategoriesAdapter.filter.filter(newText)
        }
        return true
    }

    override fun onClick(v: View?) {
        val addWordCategoryBottomSheet = AddWordCategoryBottomSheet()
        addWordCategoryBottomSheet.show(childFragmentManager, AddWordCategoryBottomSheet.ARG_ADD_WORD_CATEGORY_BS)
    }

    override fun onItemClickWordCategory(wordCategoryId: Long, wordCategoryTitle: String, wordCategoryColor: String) {
        toWordActivity(wordCategoryId, wordCategoryTitle, wordCategoryColor)
    }

    override fun itemClickRenameCategory(wordCategoryId: Long, wordCategoryTitle: String, wordCategoryColor: String) {
        TODO("Not yet implemented")
    }

    override fun itemClickDeleteCategory(wordCategoryId: Long, wordCategoryTitle: String) {
        TODO("Not yet implemented")
    }

    override fun onClickDeleteOnly(_id: Long) {
        wordCategoriesViewModel.deleteWordCategory(_id)
        wordCategoriesViewModel.deleteWordItem(_id)
    }

    override fun onClickDeleteAll() {
        wordCategoriesViewModel.deleteAllWordCategories()
        wordCategoriesViewModel.deleteAllWordItems()
    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.fabAddWordCategory.hide()
            } else {
                binding.fabAddWordCategory.show()
            }
        }
    }

    private fun changeOrderList(defaultOrderIndex: Int) {
        otherFragmentsPresenter.initView(defaultOrderIndex)
        sharedLocalPreferences.saveIntValue(KEY_ORDER_WORD_CATEGORY_INDEX, defaultOrderIndex)
    }

    private fun toWordActivity(_id: Long, categoryTitle: String, categoryColor: String) {
        TODO("Not yet implemented")
    }
}