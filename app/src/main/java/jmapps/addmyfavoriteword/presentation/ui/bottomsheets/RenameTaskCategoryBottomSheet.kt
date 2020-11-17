package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetRenameTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class RenameTaskCategoryBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetRenameTaskCategoryBinding
    private lateinit var tasksCategoryViewModel: TasksCategoryViewModel

    private var categoryId: Long? = null
    private var categoryTitle: String? = null
    private var categoryColor: String? = null
    private var newCategoryColor: String? = null

    companion object {
        const val ARG_RENAME_TASK_CATEGORY_BS = "arg_rename_task_category_bs"
        private const val ARG_RENAME_TASK_CATEGORY_ID = "arg_rename_task_category_id"
        private const val ARG_RENAME_TASK_CATEGORY_TITLE = "arg_rename_task_category_title"
        private const val ARG_RENAME_TASK_CATEGORY_COLOR = "arg_rename_task_category_color"

        @JvmStatic
        fun toInstance(
            categoryId: Long,
            categoryTitle: String,
            categoryColor: String): RenameTaskCategoryBottomSheet {
            return RenameTaskCategoryBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_RENAME_TASK_CATEGORY_ID, categoryId)
                    putString(ARG_RENAME_TASK_CATEGORY_TITLE, categoryTitle)
                    putString(ARG_RENAME_TASK_CATEGORY_COLOR, categoryColor)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksCategoryViewModel = ViewModelProvider(this).get(TasksCategoryViewModel::class.java)

        categoryId = arguments?.getLong(ARG_RENAME_TASK_CATEGORY_ID, 0)
        categoryTitle = arguments?.getString(ARG_RENAME_TASK_CATEGORY_TITLE)
        categoryColor = arguments?.getString(ARG_RENAME_TASK_CATEGORY_COLOR)

        newCategoryColor = categoryColor
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_rename_task_category, container, false)

        binding.editRenameTaskCategory.setText(categoryTitle)
        binding.editRenameTaskCategory.setSelection(categoryTitle!!.length)
        binding.textCurrentCategoryColor.setBackgroundColor(Color.parseColor(categoryColor))

        binding.textCurrentCategoryColor.setOnClickListener(this)
        binding.buttonRenameTaskCategory.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_current_category_color -> {
                MaterialColorPickerDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.description_choose_color))
                    .setColorRes(resources.getIntArray(R.array.themeColors).toList())
                    .setColorListener { _, colorHex ->
                        binding.textCurrentCategoryColor.setBackgroundColor(Color.parseColor(colorHex))
                        newCategoryColor = colorHex
                    }
                    .show()
            }

            R.id.button_rename_task_category -> {
                checkName()
            }
        }
    }

    private fun checkName() {
        when {
            binding.editRenameTaskCategory.text.toString().isEmpty() -> {
                binding.editRenameTaskCategory.error = getString(R.string.hint_enter_new_category_name)
            }
            binding.editRenameTaskCategory.text.toString().isNotEmpty() &&
                    binding.editRenameTaskCategory.text.toString() != categoryTitle ||
                        categoryColor != newCategoryColor -> {
                renameTaskCategory()
                Toast.makeText(requireContext(), getString(R.string.toast_category_renamed), Toast.LENGTH_SHORT).show()
            }
            else -> {
                dialog?.dismiss()
            }
        }
    }

    private fun renameTaskCategory() {
        tasksCategoryViewModel.updateTaskCategory(
            binding.editRenameTaskCategory.text.toString(),
            newCategoryColor!!,
            MainOther().currentTime,
            categoryId!!
        )

        if (newCategoryColor != categoryColor) {
            tasksCategoryViewModel.updateTaskItemColor(newCategoryColor!!, categoryId!!)
        }
        dialog?.dismiss()
    }
}