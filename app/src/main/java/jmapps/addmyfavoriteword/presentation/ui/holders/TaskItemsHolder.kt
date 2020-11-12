package jmapps.addmyfavoriteword.presentation.ui.holders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.presentation.ui.adapters.TaskItemsAdapter

class TaskItemsHolder(taskView: View) : RecyclerView.ViewHolder(taskView) {
    val taskItemColor: TextView = taskView.findViewById(R.id.text_task_item_color)
    val taskItemCheckBox: CheckBox = taskView.findViewById(R.id.checkbox_task_state)
    val taskItemTitle: TextView = taskView.findViewById(R.id.text_title_task)

    fun findCheckboxChecked(onTaskCheckboxState: TaskItemsAdapter.OnTaskCheckboxState, _id: Long) {
        taskItemCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskCheckboxState.onTaskCheckboxState(_id, isChecked)
        }
    }
}