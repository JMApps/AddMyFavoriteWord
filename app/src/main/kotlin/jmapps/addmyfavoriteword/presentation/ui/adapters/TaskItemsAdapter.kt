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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.presentation.ui.holders.TaskItemsHolder

class TaskItemsAdapter(
    private val context: Context,
    private var taskItemList: MutableList<TaskItems>,
    private val onTaskCheckboxState: OnTaskCheckboxState,
    private val onLongClickTaskItem: OnItemClickTaskItem) : RecyclerView.Adapter<TaskItemsHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var firstTaskItemList: MutableList<TaskItems>? = null

    init {
        this.firstTaskItemList = taskItemList
    }

    interface OnTaskCheckboxState {
        fun onTaskCheckboxState(taskItemId: Long, state: Boolean)
    }

    interface OnItemClickTaskItem {
        fun itemClickRenameItem(taskItemId: Long, taskItemTitle: String, priority: Long)
        fun itemClickDeleteItem(taskItemId: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemsHolder {
        val viewItem = inflater.inflate(R.layout.item_task, parent, false)
        return (TaskItemsHolder(viewItem))
    }

    override fun onBindViewHolder(holder: TaskItemsHolder, position: Int) {
        val current = taskItemList[position]

        DrawableCompat.setTint(holder.tvTaskItemColor.background, Color.parseColor(current.taskItemColor))
        holder.tvTaskItemColor.text = (position + 1).toString()

        holder.tvTaskItemCheckBox.buttonTintList = ColorStateList.valueOf(Color.parseColor(current.taskItemColor))
        holder.tvTaskItemCheckBox.isChecked = current.currentTaskState

        if (current.currentTaskState) {
            holder.tvTaskItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvTaskItemTitle.text = current.taskItemTitle
        } else {
            holder.tvTaskItemTitle.text = current.taskItemTitle
        }

        val taskAddDateTime = context.getString(R.string.action_add_time_item_task, "\n${current.addDateTime}")
        val taskChangeDateTime = context.getString(R.string.action_change_time_item_task, "\n${current.changeDateTime}")

        holder.tvTaskItemAddDateTime.text = taskAddDateTime
        holder.tvTaskItemChangeDateTime.text = taskChangeDateTime

        if (current.executionDateTime != "null") {
            val taskExecutionDateTime = context.getString(R.string.action_execution_time_item_task, "\n${current.executionDateTime}")
            holder.tvTaskItemExecutionDateTime.text = taskExecutionDateTime
        } else {
            holder.tvTaskItemExecutionDateTime.text = context.getString(R.string.action_state_item_task)
        }

        val priorityName = arrayListOf("#FFFFFF", "#FFF8E1", "#E8F5E9", "#FFEBEE")
        holder.llTaskItemPriority.setBackgroundColor(Color.parseColor(priorityName[current.priority.toInt()]))

        holder.findCheckboxChecked(onTaskCheckboxState, current._id)
        holder.findOnItemClick(onLongClickTaskItem, current._id, current.taskItemTitle, current.priority)
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
                            row.taskItemTitle.toLowerCase().contains(charString.toLowerCase())) {
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