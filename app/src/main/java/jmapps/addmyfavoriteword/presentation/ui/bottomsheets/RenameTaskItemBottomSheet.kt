package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetRenameTaskItemBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class RenameTaskItemBottomSheet : BottomSheetDialogFragment(), View.OnClickListener, TextWatcher {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetRenameTaskItemBinding
    private lateinit var taskItemViewModel: TasksItemViewModel

    private var taskId: Long? = null
    private var taskTitle: String? = null
    private var taskPriority: Long? = null

    companion object {
        const val ARG_RENAME_TASK_ITEM_BS = "arg_rename_task_item_bs"
        private const val ARG_RENAME_TASK_ITEM_ID = "arg_rename_task_item_id"
        private const val ARG_RENAME_TASK_ITEM_TITLE = "arg_rename_task_item_title"
        private const val ARG_RENAME_TASK_ITEM_PRIORITY = "arg_rename_task_item_priority"

        @JvmStatic
        fun toInstance(
            taskId: Long,
            taskTitle: String,
            taskPriority: Long): RenameTaskItemBottomSheet {
            return RenameTaskItemBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_RENAME_TASK_ITEM_ID, taskId)
                    putString(ARG_RENAME_TASK_ITEM_TITLE, taskTitle)
                    putLong(ARG_RENAME_TASK_ITEM_PRIORITY, taskPriority)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)

        taskId = arguments?.getLong(ARG_RENAME_TASK_ITEM_ID)
        taskTitle = arguments?.getString(ARG_RENAME_TASK_ITEM_TITLE)
        taskPriority = arguments?.getLong(ARG_RENAME_TASK_ITEM_PRIORITY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_rename_task_item, container, false)

        retainInstance = true

        val taskItemNameCharacters = getString(R.string.max_task_item_name_characters, taskTitle!!.length)
        binding.textLengthChangeTaskItemCharacters.text = taskItemNameCharacters

        binding.editRenameTaskItem.setText(taskTitle)
        binding.editRenameTaskItem.setSelection(taskTitle!!.length)
        binding.editRenameTaskItem.addTextChangedListener(this)
        binding.buttonRenameTaskItem.setOnClickListener(this)

        binding.spinnerTaskNewPriority.setSelection(taskPriority!!.toInt())

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length!! <= 100) {
            val taskItemNameCharacters = getString(R.string.max_task_item_name_characters, s.length)
            binding.textLengthChangeTaskItemCharacters.text = taskItemNameCharacters
        }

        if (s.length == 100) {
            Toast.makeText(requireContext(), getString(R.string.toast_achieved_max_task_item_name_characters), Toast.LENGTH_SHORT).show()
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_rename_task_item -> {
                checkName()
            }
        }
    }

    private fun checkName() {
        if (binding.editRenameTaskItem.text.toString().isNotEmpty()) {
            if (binding.editRenameTaskItem.text.toString() != taskTitle ||
                binding.spinnerTaskNewPriority.selectedItemId != taskPriority) {
                val newTitle = binding.editRenameTaskItem.text.toString()
                taskItemViewModel.updateTaskTitle(
                    newTitle.trim(),
                    MainOther().currentTime,
                    binding.spinnerTaskNewPriority.selectedItemId,
                    taskId!!
                )
                Toast.makeText(requireContext(), getString(R.string.toast_task_changed), Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            } else {
                dialog?.dismiss()
            }
        } else {
            binding.editRenameTaskItem.error = getString(R.string.hint_enter_new_task_name)
        }
    }
}