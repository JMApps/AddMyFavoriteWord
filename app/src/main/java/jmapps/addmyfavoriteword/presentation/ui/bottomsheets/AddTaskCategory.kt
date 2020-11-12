package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.databinding.BottomsheetAddTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddTaskCategory : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var tasksCategoryViewModel: TasksCategoryViewModel
    private lateinit var binding: BottomsheetAddTaskCategoryBinding

    private var standardColor: String = "#E57373"
    private var standardIntermediate: String = "ci_day"

    companion object {
        const val ARG_TASK_CATEGORY_FRAGMENT = "arg_task_category_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksCategoryViewModel = ViewModelProvider(this).get(TasksCategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task_category, container, false)

        binding.textCurrentCategoryColor.setBackgroundColor(Color.parseColor(standardColor))
        binding.textCurrentCategoryColor.setOnClickListener(this)
        binding.buttonAddTaskCategory.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_current_category_color -> {
                MaterialColorPickerDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.action_choose_color))
                    .setColorRes(resources.getIntArray(R.array.themeColors).toList())
                    .setColorListener { _, colorHex ->
                        binding.textCurrentCategoryColor.setBackgroundColor(Color.parseColor(colorHex))
                        standardColor = colorHex
                    }
                    .show()
            }

            R.id.button_add_task_category -> {
               checkEditText()
            }
        }
    }

    private fun checkEditText() {
        if (binding.editAddTaskCategory.text.toString().isNotEmpty()) {
            addTaskCategory()
        } else {
            binding.editAddTaskCategory.error = getString(R.string.action_enter_category_name)
        }
    }

    private fun addTaskCategory() {
        val addTaskCategories = TaskCategories(
            0,
            binding.editAddTaskCategory.text.toString(),
            standardColor,
            standardIntermediate,
            MainOther().currentTime,
            MainOther().currentTime)
        tasksCategoryViewModel.insertTaskCategory(addTaskCategories)
        dialog?.dismiss()
    }
}