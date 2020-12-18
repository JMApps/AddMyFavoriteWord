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
import jmapps.addmyfavoriteword.data.database.room.tasks.tasks.TaskItems
import jmapps.addmyfavoriteword.databinding.BottomsheetAddTaskItemBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddTaskItemBottomSheet : BottomSheetDialogFragment(), View.OnClickListener, TextWatcher {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var taskItemViewModel: TasksItemViewModel
    private lateinit var binding: BottomsheetAddTaskItemBinding

    private var displayBy: Long = 0
    private var categoryColor: String? = null

    companion object {
        const val ARG_ADD_TASK_ITEM_BS = "arg_add_task_item_bs"
        private const val ARG_ADD_TASK_CATEGORY_ID = "arg_add_task_category_id"
        private const val ARG_ADD_TASK_CATEGORY_COLOR = "arg_add_task_category_color"

        @JvmStatic
        fun toInstance(displayBy: Long, categoryColor: String): AddTaskItemBottomSheet {
            return AddTaskItemBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ADD_TASK_CATEGORY_ID, displayBy)
                    putString(ARG_ADD_TASK_CATEGORY_COLOR, categoryColor)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskItemViewModel = ViewModelProvider(this).get(TasksItemViewModel::class.java)

        displayBy = arguments?.getLong(ARG_ADD_TASK_CATEGORY_ID)!!
        categoryColor = arguments?.getString(ARG_ADD_TASK_CATEGORY_COLOR)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task_item, container, false)

        retainInstance = true

        val taskItemNameCharacters = getString(R.string.max_task_item_name_characters, 0)
        binding.textLengthAddTaskItemCharacters.text = taskItemNameCharacters

        binding.editAddTaskItem.addTextChangedListener(this)
        binding.buttonAddTaskItem.setOnClickListener(this)

        return binding.root
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length!!.toInt() <= 100) {
            val taskItemNameCharacters = getString(R.string.max_task_item_name_characters, s.length)
            binding.textLengthAddTaskItemCharacters.text = taskItemNameCharacters
        }

        if (s.length == 100) {
            Toast.makeText(requireContext(), getString(R.string.toast_achieved_max_task_item_name_characters), Toast.LENGTH_SHORT).show()
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_add_task_item -> {
                checkEditText()
            }
        }
    }

    private fun checkEditText() = if (binding.editAddTaskItem.text.toString().isNotEmpty()) {
        Toast.makeText(requireContext(), getString(R.string.toast_task_added), Toast.LENGTH_SHORT).show()
        addTaskItem()
    } else {
        binding.editAddTaskItem.error = getString(R.string.hint_enter_task_name)
    }

    private fun addTaskItem() {
        val addTaskItems = TaskItems(
            0,
            binding.editAddTaskItem.text.toString().trim(),
            displayBy,
            categoryColor!!,
            MainOther().currentTime,
            MainOther().currentTime,
            "null",
            binding.spinnerTaskPriority.selectedItemId,
            false,
        )
        taskItemViewModel.insertTaskItem(addTaskItems)
        dialog?.dismiss()
    }
}