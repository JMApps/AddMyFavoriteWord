package jmapps.addmyfavoriteword.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.presentation.ui.holders.TaskCategoriesHolder

class TaskCategoriesAdapter(
    context: Context,
    private var taskCategoryList: MutableList<TaskCategories>,
    private val onItemClickTaskCategory: OnItemClickTaskCategory,
    private val onLongClickTaskCategory: OnLongClickTaskCategory) : RecyclerView.Adapter<TaskCategoriesHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstTaskCategoryList: MutableList<TaskCategories>? = null

    init {
        this.firstTaskCategoryList = taskCategoryList
    }

    interface OnItemClickTaskCategory {
        fun onItemClickTaskCategory(_id: Long, categoryTitle: String, categoryColor: String)
    }

    interface OnLongClickTaskCategory {
        fun itemClickRenameCategory(_id: Long, categoryTitle: String, categoryColor: String)
        fun itemClickDeleteCategory(_id: Long, categoryTitle: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoriesHolder {
        val viewCategory = inflater.inflate(R.layout.item_task_category, parent, false)
        return (TaskCategoriesHolder(viewCategory))
    }

    override fun onBindViewHolder(holder: TaskCategoriesHolder, position: Int) {
        val current = taskCategoryList[position]

        holder.taskCategoryColor.setBackgroundColor(Color.parseColor(current.categoryColor))
        holder.taskCategoryColor.text = (position + 1).toString()
        holder.taskCategoryTitle.text = current.title

        holder.findItemClick(onItemClickTaskCategory, current._id, current.title, current.categoryColor)
        holder.findLongItemClick(onLongClickTaskCategory, current._id, current.title, current.categoryColor)
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
                            row.title.toLowerCase().contains(charString.toLowerCase())
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