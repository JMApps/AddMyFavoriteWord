package jmapps.addmyfavoriteword.presentation.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.presentation.ui.holders.TaskItemsHolder

class TaskItemsAdapter(
    private val context: Context,
    private var taskItemList: MutableList<TaskItems>,
    private val onTaskCheckboxState: OnTaskCheckboxState,
    private val onLongClickTaskItem: OnLongClickTaskItem) : RecyclerView.Adapter<TaskItemsHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstTaskItemList: MutableList<TaskItems>? = null

    init {
        this.firstTaskItemList = taskItemList
    }

    interface OnTaskCheckboxState {
        fun onTaskCheckboxState(_id: Long, state: Boolean)
    }

    interface OnLongClickTaskItem {
        fun itemClickRenameItem(_id: Long, taskTitle: String)
        fun itemClickDeleteItem(_id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemsHolder {
        val viewItem = inflater.inflate(R.layout.item_task, parent, false)
        return (TaskItemsHolder(viewItem))
    }

    override fun onBindViewHolder(holder: TaskItemsHolder, position: Int) {
        val current = taskItemList[position]

        holder.taskItemColor.setBackgroundColor(Color.parseColor(current.taskColor))
        holder.taskItemColor.text = (position + 1).toString()

        holder.taskItemCheckBox.buttonTintList = ColorStateList.valueOf(Color.parseColor(current.taskColor))
        holder.taskItemCheckBox.isChecked = current.currentTaskState

        if (current.currentTaskState) {
            holder.taskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.taskItemTitle.text = current.title
        } else {
            holder.taskItemTitle.text = current.title
        }

        val taskAddDateTime = context.getString(R.string.action_add_time_item_task, current.addDateTime)
        val taskChangeDateTime = context.getString(R.string.action_change_time_item_task, current.changeDateTime)

        holder.taskItemAddDateTime.text = taskAddDateTime
        holder.taskItemChangeDateTime.text = taskChangeDateTime

        holder.findCheckboxChecked(onTaskCheckboxState, current._id)
        holder.findLongItemClick(onLongClickTaskItem, current._id, current.title)
    }

    override fun getItemCount() = taskItemList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                taskItemList = if (charString.isEmpty()) {
                    firstTaskItemList as MutableList<TaskItems>
                } else {
                    val filteredList = ArrayList<TaskItems>()
                    for (row in firstTaskItemList!!) {
                        if (row._id.toString().contains(charSequence) ||
                            row.title.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = taskItemList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                taskItemList = filterResults.values as ArrayList<TaskItems>
                notifyDataSetChanged()
            }
        }
    }
}