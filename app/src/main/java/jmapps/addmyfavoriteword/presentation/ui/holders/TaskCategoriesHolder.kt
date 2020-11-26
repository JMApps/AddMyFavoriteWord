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

    val taskCategoryColor: TextView = viewCategory.findViewById(R.id.text_current_category_color)
    val taskCategoryTitle: TextView = viewCategory.findViewById(R.id.text_title_task_category)
    val taskCategoryAddDateTime: TextView = viewCategory.findViewById(R.id.text_view_task_category_item_add_date_time)
    val taskCategoryChangeDateTime: TextView = viewCategory.findViewById(R.id.text_view_task_category_item_change_date_time)

    init {
        sharedLocalPreferences = SharedLocalProperties(preferences)
        PreferenceManager.getDefaultSharedPreferences(itemView.context)
            .registerOnSharedPreferenceChangeListener(this)
        setShowAddChangeDateTime()
    }

    fun findItemClick(
        onItemClickTaskCategory: TaskCategoriesAdapter.OnItemClickTaskCategory,
        _id: Long,
        categoryTitle: String,
        categoryColor: String) {
        itemView.setOnClickListener {
            onItemClickTaskCategory.onItemClickTaskCategory(_id, categoryTitle, categoryColor)
        }
    }

    fun findLongItemClick(
        onLongClickTaskCategory: TaskCategoriesAdapter.OnLongClickTaskCategory,
        _id: Long,
        categoryTitle: String,
        categoryColor: String) {
        itemView.setOnLongClickListener {
            val pop = PopupMenu(itemView.context, taskCategoryTitle)
            pop.inflate(R.menu.menu_change_item_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_change_item -> {
                        onLongClickTaskCategory.itemClickRenameCategory(_id, categoryTitle, categoryColor)
                    }

                    R.id.popup_delete_item -> {
                        onLongClickTaskCategory.itemClickDeleteCategory(_id, categoryTitle)
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
        val addShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskCategoryBottomSheet.KEY_TASK_CATEGORY_ADD_DATE_TIME, false)
        val changeShowingDateTime = sharedLocalPreferences.getBooleanValue(ToolsTaskCategoryBottomSheet.KEY_TASK_CATEGORY_CHANGE_DATE_TIME, false)

        if (!addShowingDateTime!!) {
            taskCategoryAddDateTime.visibility = View.GONE
        } else {
            taskCategoryAddDateTime.visibility = View.VISIBLE
        }

        if (!changeShowingDateTime!!) {
            taskCategoryChangeDateTime.visibility = View.GONE
        } else {
            taskCategoryChangeDateTime.visibility = View.VISIBLE
        }
    }
}