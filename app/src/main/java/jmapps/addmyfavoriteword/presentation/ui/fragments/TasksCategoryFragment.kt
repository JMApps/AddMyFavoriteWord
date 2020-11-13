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
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddTaskCategory
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TasksCategoryFragment : Fragment(), ContractInterface.OtherView,
    TaskCategoriesAdapter.OnItemClickTaskCategory, View.OnClickListener,
    SearchView.OnQueryTextListener {

    private lateinit var tasksCategoryViewModel: TasksCategoryViewModel
    private lateinit var binding: FragmentTasksCategoryBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    private lateinit var searchView: SearchView
    private lateinit var taskCategoriesAdapter: TaskCategoriesAdapter

    private var defaultOrderIndex = 0

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
                defaultOrderIndex = 0
                changeOrderList(defaultOrderIndex)
            }
            R.id.item_order_by_change_time -> {
                defaultOrderIndex = 1
                changeOrderList(defaultOrderIndex)
            }
            R.id.item_order_by_color -> {
                defaultOrderIndex = 2
                changeOrderList(defaultOrderIndex)
            }
            R.id.item_order_by_alphabet -> {
                defaultOrderIndex = 3
                changeOrderList(defaultOrderIndex)
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
        val addTaskCategory = AddTaskCategory()
        addTaskCategory.show(childFragmentManager, AddTaskCategory.ARG_TASK_CATEGORY_FRAGMENT)
    }

    override fun initView(sortedBy: String) {
        tasksCategoryViewModel.allTaskCategories(sortedBy).observe(this, Observer {
            it.let {
                otherFragmentsPresenter.updateState(it)
                taskCategoriesAdapter = TaskCategoriesAdapter(requireContext(), it, this)
                val verticalLayout = LinearLayoutManager(requireContext())
                binding.rvTaskCategories.layoutManager = verticalLayout
                binding.rvTaskCategories.adapter = taskCategoriesAdapter
            }
        })
    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddTaskCategory.startAnimation(show)

        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun onItemClickTaskCategory(_id: Long, categoryTitle: String, categoryColor: String) {
        toTaskActivity(_id, categoryTitle, categoryColor)
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