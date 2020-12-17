package jmapps.addmyfavoriteword.presentation.ui.holders

import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskCategoriesAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.ToolsTaskCategoryBottomSheet
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class TaskCategoriesHolder(viewCategory: View) : RecyclerView.ViewHolder(viewCategory),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private var sharedLocalPreferences: SharedLocalProperties

    val tvTaskCategoryColor: TextView = viewCategory.findViewById(R.id.text_task_category_color)
    val tvTaskCategoryTitle: TextView = viewCategory.findViewById(R.id.text_task_category_title)
    val tvTaskCategoryAddDateTime: TextView = viewCategory.findViewById(R.id.text_view_task_category_item_add_date_time)
    val tvTaskCategoryChangeDateTime: TextView = viewCategory.findViewById(R.id.text_view_task_category_item_change_date_time)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
        setShowAddChangeDateTime()
    }

    fun findItemClick(
        onItemClickTaskCategory: TaskCategoriesAdapter.OnItemClickTaskCategory,
        taskCategoryId: Long,
        taskCategoryTitle: String,
        taskCategoryColor: String) {
        itemView.setOnClickListener {
            onItemClickTaskCategory.onItemClickTaskCategory(taskCategoryId, taskCategoryTitle, taskCategoryColor)
        }
    }

    fun findLongItemClick(
        onLongClickTaskCategory: TaskCategoriesAdapter.OnLongClickTaskCategory,
        taskCategoryId: Long,
        taskCategoryTitle: String,
        taskCategoryColor: String) {
        itemView.setOnLongClickListener {
            val pop = PopupMenu(itemView.context, tvTaskCategoryTitle)
            pop.inflate(R.menu.menu_change_item_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_change_item -> {
                        onLongClickTaskCategory.itemClickRenameCategory(taskCategoryId, taskCategoryTitle, taskCategoryColor)
                    }
                    R.id.popup_delete_item -> {
                        onLongClickTaskCategory.itemClickDeleteCategory(taskCategoryId, taskCategoryTitle)
                    }
                }
                true
            }
            pop.show()
            true
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        setShowAddChangeDateTime()
    }

    private fun setShowAddChangeDateTime() {
        val addShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskCategoryBottomSheet.ARG_TASK_CATEGORY_ADD_DATE_TIME, false)
        val changeShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskCategoryBottomSheet.ARG_TASK_CATEGORY_CHANGE_DATE_TIME, false)

        if (!addShowingDateTime) {
            tvTaskCategoryAddDateTime.visibility = View.GONE
        } else {
            tvTaskCategoryAddDateTime.visibility = View.VISIBLE
        }

        if (!changeShowingDateTime) {
            tvTaskCategoryChangeDateTime.visibility = View.GONE
        } else {
            tvTaskCategoryChangeDateTime.visibility = View.VISIBLE
        }
    }
}