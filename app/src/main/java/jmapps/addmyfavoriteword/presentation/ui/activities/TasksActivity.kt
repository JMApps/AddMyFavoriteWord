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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.ActivityTaskBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherActivities.OtherActivityPresenter
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddTaskItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.RenameTaskItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsTaskItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.AlertUtil
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TasksActivity : AppCompatActivity(), ContractInterface.OtherView,
    SearchView.OnQueryTextListener, TaskItemsAdapter.OnTaskCheckboxState, View.OnClickListener,
    TaskItemsAdapter.OnLongClickTaskItem, AlertUtil.OnClickDelete {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskItemViewModel: TasksItemViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties
    private lateinit var otherActivityPresenter: OtherActivityPresenter

    private lateinit var searchView: SearchView
    private lateinit var taskItemsAdapter: TaskItemsAdapter

    private var taskCategoryId: Long? = null
    private var taskCategoryTitle: String? = null
    private var taskCategoryColor: String? = null
    private var defaultOrderIndex: Int? = null

    private lateinit var alertDialog: AlertUtil

    companion object {
        const val KEY_TASK_CATEGORY_ID = "key_task_category_id"
        const val KEY_TASK_CATEGORY_TITLE = "key_task_category_title"
        const val KEY_TASK_CATEGORY_COLOR = "key_task_category_color"
        private const val KEY_ORDER_TASK_INDEX = "key_order_task_index"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task)
        setSupportActionBar(binding.toolbar)

        taskCategoryId = intent.getLongExtra(KEY_TASK_CATEGORY_ID, 0)
        taskCategoryTitle = intent.getStringExtra(KEY_TASK_CATEGORY_TITLE)
        taskCategoryColor = intent.getStringExtra(KEY_TASK_CATEGORY_COLOR)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedLocalPreferences = SharedLocalProperties(preferences)
        defaultOrderIndex = sharedLocalPreferences.getIntValue(KEY_ORDER_TASK_INDEX, 0)!!

        otherActivityPresenter = OtherActivityPresenter(this)
        otherActivityPresenter.initView(taskCategoryId!!, defaultOrderIndex!!)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = taskCategoryTitle
        }

        alertDialog = AlertUtil(this, this)

        binding.taskItemContent.rvTaskItems.addOnScrollListener(onAddScroll)
        binding.taskItemContent.fabAddTaskItem.setOnClickListener(this)
    }

    override fun initView(displayBy: Long, orderBy: String) {
        taskItemViewModel.allTaskItems(displayBy, orderBy).observe(this, {
            it.let {
                val verticalLayout = LinearLayoutManager(this)
                binding.taskItemContent.rvTaskItems.layoutManager = verticalLayout
                taskItemsAdapter = TaskItemsAdapter(this, it, this, this)
                binding.taskItemContent.rvTaskItems.adapter = taskItemsAdapter
                otherActivityPresenter.updateState(it)
                otherActivityPresenter.defaultState()
            }
        })
    }

    override fun defaultState() {
        binding.taskItemContent.rvTaskItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.taskItemContent.textMainTaskDescription.visibility = otherActivityPresenter.descriptionMain()
        val categoryDescription = getString(R.string.description_add_first_task, "<b>$taskCategoryTitle</b>")
        binding.taskItemContent.textMainTaskDescription.text = Html.fromHtml(categoryDescription)
    }

    override fun updateState() {
        binding.taskItemContent.rvTaskItems.visibility = otherActivityPresenter.recyclerCategory()
        binding.taskItemContent.textMainTaskDescription.visibility = otherActivityPresenter.descriptionMain()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_task_items, menu)
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_tasks).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_tools_task_items -> {
                val toolsTaskItem = ToolsTaskItemBottomSheet()
                toolsTaskItem.show(supportFragmentManager, ToolsTaskItemBottomSheet.ARG_TOOLS_TASK_ITEM_BS)
            }
            R.id.item_order_by_add_time -> {
                changeOrderList(defaultOrderIndex = 0)
            }
            R.id.item_order_by_execution -> {
                changeOrderList(defaultOrderIndex = 1)
            }
            R.id.item_order_by_alphabet -> {
                changeOrderList(defaultOrderIndex = 2)
            }
            R.id.item_order_by_priority -> {
                changeOrderList(defaultOrderIndex = 3)
            }
            R.id.action_delete_all_task_items -> {
                val deleteAllTaskDescription = getString(
                    R.string.dialog_message_are_sure_you_want_items_task,
                    "<b>$taskCategoryTitle</b>"
                )
                alertDialog.showAlertDialog(deleteAllTaskDescription, 0, 0, getString(R.string.action_tasks_deleted))
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
        val addTaskItem = AddTaskItemBottomSheet.toInstance(taskCategoryId!!, taskCategoryColor!!)
        addTaskItem.show(supportFragmentManager, AddTaskItemBottomSheet.ARG_ADD_TASK_ITEM_BS)
    }

    override fun itemClickRenameItem(_id: Long, taskTitle: String, taskPriority: Long) {
        val renameTaskItem = RenameTaskItemBottomSheet.toInstance(_id, taskTitle, taskPriority)
        renameTaskItem.show(supportFragmentManager, RenameTaskItemBottomSheet.ARG_RENAME_TASK_ITEM_BS)
    }

    override fun onClickDeleteAll() {
        taskItemViewModel.deleteAllTaskFromCategory(taskCategoryId!!)
    }

    override fun onClickDeleteOnly(_id: Long) {
        taskItemViewModel.deleteTaskItem(_id)
    }

    override fun itemClickDeleteItem(_id: Long) {
        alertDialog.showAlertDialog(
            getString(R.string.dialog_message_are_sure_you_want_item_task), 1, _id, getString(R.string.action_task_deleted)
        )
    }

    override fun onTaskCheckboxState(_id: Long, state: Boolean) {
        if (state) {
            taskItemViewModel.updateState(state, MainOther().currentTime, _id)
        } else {
            taskItemViewModel.updateState(state, "null",  _id)
        }
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
        sharedLocalPreferences.saveIntValue(KEY_ORDER_TASK_INDEX, defaultOrderIndex)
        otherActivityPresenter.initView(taskCategoryId!!, defaultOrderIndex)
        recreate()
    }
}