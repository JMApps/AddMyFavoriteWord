package jmapps.addmyfavoriteword.presentation.ui.activities

import android.app.SearchManager
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityTaskBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.OtherActivityPresenter
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TasksActivity : AppCompatActivity(), ContractInterface.OtherView,
    SearchView.OnQueryTextListener, TaskItemsAdapter.OnTaskCheckboxState, View.OnClickListener {

    private lateinit var taskItemViewModel: TasksItemViewModel
    private lateinit var binding: ActivityTaskBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherActivityPresenter: OtherActivityPresenter

    private lateinit var searchView: SearchView
    private lateinit var taskItemsAdapter: TaskItemsAdapter

    private var taskCategoryId: Long = 0
    private var defaultOrderIndex = 0

    companion object {
        const val keyTaskCategoryId = "key_task_category_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskCategoryId = intent.getLongExtra(keyTaskCategoryId, 0)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedLocalPreferences = SharedLocalProperties(preferences)

        defaultOrderIndex = sharedLocalPreferences.getIntValue("order_task_index", 0)!!

        otherActivityPresenter = OtherActivityPresenter(this)
        otherActivityPresenter.initView(taskCategoryId, defaultOrderIndex)
        otherActivityPresenter.defaultState()

        binding.taskItemContent.rvTaskItems.addOnScrollListener(onAddScroll)
        binding.taskItemContent.fabAddTaskItem.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_items, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_categories).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.item_order_by_add_time -> {
                defaultOrderIndex = 0
                changeOrderList(defaultOrderIndex)
            }
            R.id.item_order_by_execution -> {
                defaultOrderIndex = 1
                changeOrderList(defaultOrderIndex)
            }
            R.id.item_order_by_alphabet -> {
                defaultOrderIndex = 2
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
            taskItemsAdapter.filter.filter("")
        } else {
            taskItemsAdapter.filter.filter(newText)
        }
        return true
    }

    override fun onClick(v: View?) {

    }

    override fun initView(displayBy: Long, sortedBy: String) {
        taskItemViewModel.allTaskItems(displayBy, sortedBy).observe(this, {
            it.let {
                taskItemsAdapter = TaskItemsAdapter(this, it, this)
                val verticalLayout = LinearLayoutManager(this)
                binding.taskItemContent.rvTaskItems.layoutManager = verticalLayout
                binding.taskItemContent.rvTaskItems.adapter = taskItemsAdapter
                otherActivityPresenter.updateState(it)
            }
        })
    }

    override fun defaultState() {
        binding.taskItemContent.rvTaskItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.taskItemContent.textMainTaskDescription.visibility =
            otherActivityPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.taskItemContent.rvTaskItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.taskItemContent.textMainTaskDescription.visibility =
            otherActivityPresenter.descriptionMain()
    }

    override fun onTaskCheckboxState(_id: Long, state: Boolean) {

    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.taskItemContent.fabAddTaskItem.hide()
            } else {
                binding.taskItemContent.fabAddTaskItem.show()
            }
        }
    }

    private fun changeOrderList(defaultOrderIndex: Int) {
        otherActivityPresenter.initView(taskCategoryId, defaultOrderIndex)
        sharedLocalPreferences.saveIntValue("order_task_index", defaultOrderIndex)
    }
}