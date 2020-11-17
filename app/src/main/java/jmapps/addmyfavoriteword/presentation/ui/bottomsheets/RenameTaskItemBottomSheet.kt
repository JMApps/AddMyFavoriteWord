package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomSheetRenameTaskItemBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class RenameTaskItemBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomSheetRenameTaskItemBinding
    private lateinit var taskItemViewModel: TasksItemViewModel

    private var taskId: Long? = null
    private var taskTitle: String? = null

    companion object {
        const val ARG_RENAME_TASK_ITEM_BS = "arg_rename_task_item_bs"
        private const val ARG_RENAME_TASK_ITEM_ID = "arg_rename_task_item_id"
        private const val ARG_RENAME_TASK_ITEM_TITLE = "arg_rename_task_item_title"

        @JvmStatic
        fun toInstance(
            taskId: Long,
            taskTitle: String): RenameTaskItemBottomSheet {
            return RenameTaskItemBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_RENAME_TASK_ITEM_ID, taskId)
                    putString(ARG_RENAME_TASK_ITEM_TITLE, taskTitle)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)

        taskId = arguments?.getLong(ARG_RENAME_TASK_ITEM_ID)
        taskTitle = arguments?.getString(ARG_RENAME_TASK_ITEM_TITLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_rename_task_item, container, false)

        binding.editRenameTaskCategory.setText(taskTitle)
        binding.editRenameTaskCategory.setSelection(taskTitle!!.length)
        binding.buttonRenameTaskItem.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        checkName()
    }

    private fun checkName() {
        if (binding.editRenameTaskCategory.text.toString().isNotEmpty()) {
            if (binding.editRenameTaskCategory.text.toString() != taskTitle) {
                val newTitle = binding.editRenameTaskCategory.text.toString()
                taskItemViewModel.updateTaskTitle(newTitle, MainOther().currentTime, taskId!!)
                Toast.makeText(requireContext(), getString(R.string.description_renamed_task), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            } else {
                dialog?.dismiss()
            }
        } else {
            binding.editRenameTaskCategory.error = getString(R.string.hint_enter_new_task_name)
        }
    }
}