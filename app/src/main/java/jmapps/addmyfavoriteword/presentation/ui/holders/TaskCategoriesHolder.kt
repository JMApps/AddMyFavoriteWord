package jmapps.addmyfavoriteword.presentation.ui.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskCategoriesAdapter

class TaskCategoriesHolder(viewCategory: View) : RecyclerView.ViewHolder(viewCategory) {

    val taskCategoryColor: TextView = viewCategory.findViewById(R.id.text_current_category_color)
    val taskCategoryTitle: TextView = viewCategory.findViewById(R.id.text_title_task_category)

    fun findItemClick(onItemClickTaskCategory: TaskCategoriesAdapter.OnItemClickTaskCategory, _id: Long) {
        itemView.setOnClickListener {
            onItemClickTaskCategory.onItemClickTaskCategory(_id)
        }
    }
}