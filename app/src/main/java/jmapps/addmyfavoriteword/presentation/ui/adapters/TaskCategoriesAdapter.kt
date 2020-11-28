package jmapps.addmyfavoriteword.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.presentation.ui.holders.TaskCategoriesHolder

class TaskCategoriesAdapter(
    private val context: Context,
    private var taskCategoryList: MutableList<TaskCategories>,
    private val onItemClickTaskCategory: OnItemClickTaskCategory,
    private val onLongClickTaskCategory: OnLongClickTaskCategory) : RecyclerView.Adapter<TaskCategoriesHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstTaskCategoryList: MutableList<TaskCategories>? = null

    init {
        this.firstTaskCategoryList = taskCategoryList
    }

    interface OnItemClickTaskCategory {
        fun onItemClickTaskCategory(
            taskCategoryId: Long,
            taskCategoryTitle: String,
            taskCategoryColor: String
        )
    }

    interface OnLongClickTaskCategory {
        fun itemClickRenameCategory(
            taskCategoryId: Long,
            taskCategoryTitle: String,
            taskCategoryColor: String
        )

        fun itemClickDeleteCategory(taskCategoryId: Long, taskCategoryTitle: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoriesHolder {
        val viewCategory = inflater.inflate(R.layout.item_task_category, parent, false)
        return (TaskCategoriesHolder(viewCategory))
    }

    override fun onBindViewHolder(holder: TaskCategoriesHolder, position: Int) {
        val current = taskCategoryList[position]

        DrawableCompat.setTint(holder.tvTaskCategoryColor.background, Color.parseColor(current.taskCategoryColor))
        holder.tvTaskCategoryColor.text = (position + 1).toString()
        holder.tvTaskCategoryTitle.text = current.taskCategoryTitle

        val taskCategoryAddDateTime = context.getString(R.string.action_add_time_item_task, "\n${current.addDateTime}")
        val taskCategoryChangeDateTime = context.getString(R.string.action_change_time_item_task, "\n${current.changeDateTime}")

        holder.tvTaskCategoryAddDateTime.text = taskCategoryAddDateTime
        holder.tvTaskCategoryChangeDateTime.text = taskCategoryChangeDateTime

        holder.findItemClick(
            onItemClickTaskCategory,
            current._id,
            current.taskCategoryTitle,
            current.taskCategoryColor
        )
        holder.findLongItemClick(
            onLongClickTaskCategory,
            current._id,
            current.taskCategoryTitle,
            current.taskCategoryColor
        )
    }

    override fun getItemCount() = taskCategoryList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                taskCategoryList = if (charString.isEmpty()) {
                    firstTaskCategoryList as MutableList<TaskCategories>
                } else {
                    val filteredList = ArrayList<TaskCategories>()
                    for (row in firstTaskCategoryList!!) {
                        if (row._id.toString().contains(charSequence) ||
                            row.taskCategoryTitle.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = taskCategoryList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                taskCategoryList = filterResults.values as ArrayList<TaskCategories>
                notifyDataSetChanged()
            }
        }
    }
}