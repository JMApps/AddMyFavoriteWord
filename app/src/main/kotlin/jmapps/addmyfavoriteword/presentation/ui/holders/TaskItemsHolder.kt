package jmapps.addmyfavoriteword.presentation.ui.holders

import android.content.SharedPreferences
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskItemsAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsTaskItemBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TaskItemsHolder(taskView: View) : RecyclerView.ViewHolder(taskView),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private var sharedLocalPreferences: SharedLocalProperties

    val llTaskItemPriority: LinearLayoutCompat = taskView.findViewById(R.id.layout_task_item_priority)
    val tvTaskItemColor: TextView = taskView.findViewById(R.id.text_task_item_color)
    val tvTaskItemCheckBox: CheckBox = taskView.findViewById(R.id.checkbox_task_state)
    val tvTaskItemTitle: TextView = taskView.findViewById(R.id.text_task_item_title)
    val tvTaskItemAddDateTime: TextView = taskView.findViewById(R.id.text_view_task_item_add_date_time)
    val tvTaskItemChangeDateTime: TextView = taskView.findViewById(R.id.text_view_task_item_change_date_time)
    val tvTaskItemExecutionDateTime: TextView = taskView.findViewById(R.id.text_view_task_item_execution_date_time)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
        setTextSize()
        setShowAddChangeDateTime()
    }

    fun findCheckboxChecked(onTaskCheckboxState: TaskItemsAdapter.OnTaskCheckboxState, taskItemId: Long) {
        tvTaskItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskCheckboxState.onTaskCheckboxState(taskItemId, isChecked)
        }
    }

    fun findOnItemClick(onItemLongClickTaskItem: TaskItemsAdapter.OnItemClickTaskItem, taskItemId: Long, taskItemTitle: String, priority: Long) {
        itemView.setOnClickListener {
            val pop = PopupMenu(itemView.context, tvTaskItemTitle)
            pop.inflate(R.menu.menu_change_item_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_change_item -> {
                        onItemLongClickTaskItem.itemClickRenameItem(taskItemId, taskItemTitle, priority)
                    }

                    R.id.popup_delete_item -> {
                        onItemLongClickTaskItem.itemClickDeleteItem(taskItemId)
                    }
                }
                true
            }
            pop.show()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setTextSize()
        setShowAddChangeDateTime()
    }

    private fun setTextSize() {
        val textSize = sharedLocalPreferences.getIntValue(ToolsTaskItemBottomSheet.ARG_TASK_ITEM_TEXT_SIZE, 18)
        tvTaskItemTitle.textSize = textSize.toFloat()
    }

    private fun setShowAddChangeDateTime() {
        val addShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskItemBottomSheet.ARG_TASK_ITEM_ADD_DATE_TIME, false)
        val changeShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskItemBottomSheet.ARG_TASK_ITEM_CHANGE_DATE_TIME, false)
        val executionShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskItemBottomSheet.ARG_TASK_ITEM_EXECUTION_DATE_TIME, false)

        if (!addShowingDateTime) {
            tvTaskItemAddDateTime.visibility = View.GONE
        } else {
            tvTaskItemAddDateTime.visibility = View.VISIBLE
        }

        if (!changeShowingDateTime) {
            tvTaskItemChangeDateTime.visibility = View.GONE
        } else {
            tvTaskItemChangeDateTime.visibility = View.VISIBLE
        }

        if (!executionShowingDateTime) {
            tvTaskItemExecutionDateTime.visibility = View.GONE
        } else {
            tvTaskItemExecutionDateTime.visibility = View.VISIBLE
        }
    }
}