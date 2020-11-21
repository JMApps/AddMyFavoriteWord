package jmapps.addmyfavoriteword.presentation.ui.holders

import android.content.SharedPreferences
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
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

    val taskItemColor: TextView = taskView.findViewById(R.id.text_task_item_color)
    val taskItemCheckBox: CheckBox = taskView.findViewById(R.id.checkbox_task_state)
    val taskItemTitle: TextView = taskView.findViewById(R.id.text_title_task)
    val taskItemAddDateTime: TextView = taskView.findViewById(R.id.text_view_task_item_add_date_time)
    val taskItemChangeDateTime: TextView = taskView.findViewById(R.id.text_view_task_item_change_date_time)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
        setTextSize()
        setShowAddChangeDateTime()
    }

    fun findCheckboxChecked(onTaskCheckboxState: TaskItemsAdapter.OnTaskCheckboxState, _id: Long) {
        taskItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskCheckboxState.onTaskCheckboxState(_id, isChecked)
        }
    }

    fun findLongItemClick(onItemLongClickTaskItem: TaskItemsAdapter.OnLongClickTaskItem, _id: Long, taskTitle: String) {
        itemView.setOnLongClickListener {
            val pop = PopupMenu(itemView.context, taskItemTitle)
            pop.inflate(R.menu.menu_change_item_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_change_item -> {
                        onItemLongClickTaskItem.itemClickRenameItem(_id, taskTitle)
                    }

                    R.id.popup_delete_item -> {
                        onItemLongClickTaskItem.itemClickDeleteItem(_id)
                    }
                }
                true
            }
            pop.show()
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setTextSize()
        setShowAddChangeDateTime()
    }

    private fun setTextSize() {
        val textSize = sharedLocalPreferences.getIntValue(ToolsTaskItemBottomSheet.KEY_TASK_ITEM_TEXT_SIZE, 18)
        taskItemTitle.textSize = textSize!!.toFloat()
    }

    private fun setShowAddChangeDateTime() {
        val addShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskItemBottomSheet.KEY_TASK_ITEM_ADD_DATE_TIME, false)
        val changeShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskItemBottomSheet.KEY_TASK_ITEM_CHANGE_DATE_TIME, false)

        if (!addShowingDateTime!!) {
            taskItemAddDateTime.visibility = View.GONE
        } else {
            taskItemAddDateTime.visibility = View.VISIBLE
        }

        if (!changeShowingDateTime!!) {
            taskItemChangeDateTime.visibility = View.GONE
        } else {
            taskItemChangeDateTime.visibility = View.VISIBLE
        }
    }
}