package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.databinding.BottomsheetAddTaskItemBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddTaskItem : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var taskItemViewModel: TasksItemViewModel
    private lateinit var binding: BottomsheetAddTaskItemBinding

    private var displayBy: Long = 0
    private var color: String = "#E57373"

    companion object {
        const val ARG_TASK_ITEM_FRAGMENT = "arg_task_item_fragment"
        private const val ARG_TASK_CATEGORY_ID = "arg_task_category_id"
        private const val ARG_TASK_CATEGORY_COLOR = "arg_task_category_color"

        @JvmStatic
        fun toInstance(displayBy: Long, color: String): AddTaskItem {
            return AddTaskItem().apply {
                arguments = Bundle().apply {
                    putLong(ARG_TASK_CATEGORY_ID, displayBy)
                    putString(ARG_TASK_CATEGORY_COLOR, color)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)

        displayBy = arguments?.getLong(ARG_TASK_CATEGORY_ID)!!
        color = arguments?.getString(ARG_TASK_CATEGORY_COLOR)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task_item, container, false)
        binding.buttonAddTaskItem.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_task_item -> {
                checkEditText()
            }
        }
    }

    private fun checkEditText() {
        if (binding.editAddTaskItem.text.toString().isNotEmpty()) {
            addTaskCategory()
        } else {
            binding.editAddTaskItem.error = getString(R.string.action_enter_task_name)
        }
    }

    private fun addTaskCategory() {
        val addTaskItems = TaskItems(
            0,
            binding.editAddTaskItem.text.toString(),
            displayBy,
            color,
            MainOther().currentTime,
            MainOther().currentTime,
            MainOther().currentTime,
            false,
        )
        taskItemViewModel.insertTaskItem(addTaskItems)
        dialog?.dismiss()
    }
}