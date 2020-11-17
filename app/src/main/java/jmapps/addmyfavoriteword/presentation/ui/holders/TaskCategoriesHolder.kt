package jmapps.addmyfavoriteword.presentation.ui.holders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskCategoriesAdapter

class TaskCategoriesHolder(viewCategory: View) : RecyclerView.ViewHolder(viewCategory) {

    val taskCategoryColor: TextView = viewCategory.findViewById(R.id.text_current_category_color)
    val taskCategoryTitle: TextView = viewCategory.findViewById(R.id.text_title_task_category)

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
            pop.inflate(R.menu.menu_task_category_popup)
            pop.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.popup_task_category_rename -> {
                        onLongClickTaskCategory.itemClickRenameCategory(_id, categoryTitle, categoryColor)
                    }

                    R.id.popup_task_category_delete -> {
                        onLongClickTaskCategory.itemClickDeleteCategory(_id, categoryTitle)
                    }
                }
                true
            }
            pop.show()
            true
        }
    }
}