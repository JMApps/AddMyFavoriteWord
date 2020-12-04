package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetRenameTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class RenameTaskCategoryBottomSheet : BottomSheetDialogFragment(), View.OnClickListener,
    TextWatcher {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_rename_task_category, container, false)

        retainInstance = true

        binding.editRenameTaskCategory.setText(categoryTitle)
        binding.editRenameTaskCategory.setSelection(categoryTitle!!.length)
        DrawableCompat.setTint(binding.textNewTaskCategoryColor.background, Color.parseColor(categoryColor))

        val categoryNameCharacters = getString(R.string.max_task_category_name_characters, categoryTitle!!.length)
        binding.textLengthChangeTaskCategoryCharacters.text = categoryNameCharacters

        binding.editRenameTaskCategory.addTextChangedListener(this)
        binding.textNewTaskCategoryColor.setOnClickListener(this)
        binding.buttonRenameTaskCategory.setOnClickListener(this)

        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length!! <= 100) {
            val categoryNameCharacters = getString(R.string.max_task_category_name_characters, s.length)
            binding.textLengthChangeTaskCategoryCharacters.text = categoryNameCharacters
        }

        if (s.length == 100) {
            Toast.makeText(requireContext(), getString(R.string.toast_achieved_max_task_category_name_characters), Toast.LENGTH_SHORT).show()
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_new_task_category_color -> {
                MaterialColorPickerDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.description_choose_color))
                    .setColorRes(resources.getIntArray(R.array.themeColors).toList())
                    .setColorListener { _, colorHex ->
                        newCategoryColor = colorHex
                        DrawableCompat.setTint(binding.textNewTaskCategoryColor.background, Color.parseColor(newCategoryColor))
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
                Toast.makeText(requireContext(), getString(R.string.toast_category_changed), Toast.LENGTH_SHORT).show()
            }
            else -> {
                dialog?.dismiss()
            }
        }
    }

    private fun renameTaskCategory() {
        tasksCategoryViewModel.updateTaskCategory(
            binding.editRenameTaskCategory.text.toString().trim(),
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