package jmapps.addmyfavoriteword.presentation.ui.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapter.TaskCategoriesAdapter

class TaskCategoriesHolder(viewCategory: View) : RecyclerView.ViewHolder(viewCategory) {

    val taskCategoryId: TextView = viewCategory.findViewById(R.id.text_id_task_category)
    val taskCategoryTitle: TextView = viewCategory.findViewById(R.id.text_title_task_category)

    fun findItemClick(onItemClickTaskCategory: TaskCategoriesAdapter.OnItemClickTaskCategory, _id: Long) {
        itemView.setOnClickListener {
            onItemClickTaskCategory.onItemClickTaskCategory(_id)
        }
    }
}