package jmapps.addmyfavoriteword.presentation.ui.fragments

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentTasksCategoryBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter
import jmapps.addmyfavoriteword.presentation.ui.activities.TasksActivity
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskCategoriesAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddTaskCategoryBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.RenameTaskCategoryBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.AlertUtil
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TasksCategoryFragment : Fragment(), ContractInterface.OtherView,
    TaskCategoriesAdapter.OnItemClickTaskCategory, View.OnClickListener,
    SearchView.OnQueryTextListener, TaskCategoriesAdapter.OnLongClickTaskCategory,
    AlertUtil.OnClickDelete {

    private lateinit var binding: FragmentTasksCategoryBinding
    private lateinit var tasksCategoryViewModel: TasksCategoryViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    private lateinit var searchView: SearchView
    private lateinit var taskCategoriesAdapter: TaskCategoriesAdapter

    private var defaultOrderIndex = 0
    private lateinit var alertDialog: AlertUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksCategoryViewModel = ViewModelProvider(this).get(TasksCategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_category, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        retainInstance = true
        setHasOptionsMenu(true)

        alertDialog = AlertUtil(requireContext(), this)

        defaultOrderIndex = sharedLocalPreferences.getIntValue("order_category_task_index", 0)!!

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.initView(defaultOrderIndex)
        otherFragmentsPresenter.defaultState()

        binding.rvTaskCategories.addOnScrollListener(onAddScroll)
        binding.fabAddTaskCategory.setOnClickListener(this)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_categories, menu)
        val searchManager = requireContext().getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_categories).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_order_by_add_time -> {
                changeOrderList(defaultOrderIndex = 0)
            }
            R.id.item_order_by_change_time -> {
                changeOrderList(defaultOrderIndex = 1)
            }
            R.id.item_order_by_color -> {
                changeOrderList(defaultOrderIndex = 2)
            }
            R.id.item_order_by_alphabet -> {
                changeOrderList(defaultOrderIndex = 3)
            }
            R.id.action_delete_all_categories -> {
                alertDialog.showAlertDialog(
                    getString(R.string.dialog_message_are_sure_you_want_task_categories), 0, 0
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            taskCategoriesAdapter.filter.filter("")
        } else {
            taskCategoriesAdapter.filter.filter(newText)
        }
        return true
    }

    override fun onClick(v: View?) {
        val addTaskCategory = AddTaskCategoryBottomSheet()
        addTaskCategory.show(childFragmentManager, AddTaskCategoryBottomSheet.ARG_ADD_TASK_CATEGORY_BS)
    }

    override fun initView(sortedBy: String) {
        tasksCategoryViewModel.allTaskCategories(sortedBy).observe(this, Observer {
            it.let {
                val verticalLayout = LinearLayoutManager(requireContext())
                binding.rvTaskCategories.layoutManager = verticalLayout
                taskCategoriesAdapter = TaskCategoriesAdapter(requireContext(), it, this, this)
                binding.rvTaskCategories.adapter = taskCategoriesAdapter
                otherFragmentsPresenter.updateState(it)
            }
        })
    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddTaskCategory.startAnimation(show)

        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainTaskContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainTaskContainerDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun onItemClickTaskCategory(_id: Long, categoryTitle: String, categoryColor: String) {
        toTaskActivity(_id, categoryTitle, categoryColor)
    }

    override fun itemClickRenameCategory(_id: Long, categoryTitle: String, categoryColor: String) {
        val renameTaskCategory = RenameTaskCategoryBottomSheet.toInstance(_id, categoryTitle, categoryColor)
        renameTaskCategory.show(childFragmentManager, RenameTaskCategoryBottomSheet.ARG_RENAME_TASK_CATEGORY_BS)
    }

    override fun itemClickDeleteCategory(_id: Long, categoryTitle: String) {
        val deleteTaskCategoryDescription = getString(R.string.dialog_message_are_sure_you_want_category, "<b>$categoryTitle</b>")
        alertDialog.showAlertDialog(deleteTaskCategoryDescription, 1, _id)

    }

    override fun onClickDeleteOnly(_id: Long) {
        tasksCategoryViewModel.deleteTaskCategory(_id)
        tasksCategoryViewModel.deleteTaskItem(_id)
    }

    override fun onClickDeleteAll() {
        tasksCategoryViewModel.deleteAllTaskCategories()
        tasksCategoryViewModel.deleteAllTaskItem()
    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.fabAddTaskCategory.hide()
            } else {
                binding.fabAddTaskCategory.show()
            }
        }
    }

    private fun changeOrderList(defaultOrderIndex: Int) {
        otherFragmentsPresenter.initView(defaultOrderIndex)
        sharedLocalPreferences.saveIntValue("order_category_task_index", defaultOrderIndex)
    }

    private fun toTaskActivity(_id: Long, categoryTitle: String, categoryColor: String) {
        val toTaskActivity = Intent(requireContext(), TasksActivity::class.java)
        toTaskActivity.putExtra(TasksActivity.keyTaskCategoryId, _id)
        toTaskActivity.putExtra(TasksActivity.keyTaskCategoryTitle, categoryTitle)
        toTaskActivity.putExtra(TasksActivity.keyTaskCategoryColor, categoryColor)
        startActivity(toTaskActivity)
    }
}