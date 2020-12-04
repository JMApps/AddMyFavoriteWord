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
import jmapps.addmyfavoriteword.data.database.room.tasks.categories.TaskCategories
import jmapps.addmyfavoriteword.databinding.BottomsheetAddTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.models.TasksCategoryViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class AddTaskCategoryBottomSheet : BottomSheetDialogFragment(), View.OnClickListener, TextWatcher {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetAddTaskCategoryBinding
    private lateinit var tasksCategoryViewModel: TasksCategoryViewModel

    private var standardColor: String = "#e57373"
    private var standardIntermediate: String = "ci_day"

    companion object {
        const val ARG_ADD_TASK_CATEGORY_BS = "arg_add_task_category_bs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksCategoryViewModel = ViewModelProvider(this).get(TasksCategoryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task_category, container, false)

        retainInstance = true

        DrawableCompat.setTint(binding.textAddTaskCategoryColor.background, Color.parseColor(standardColor))

        val categoryNameCharacters = getString(R.string.max_task_category_name_characters, 0)
        binding.textLengthAddTaskCategoryCharacters.text = categoryNameCharacters

        binding.editAddTaskCategory.addTextChangedListener(this)
        binding.textAddTaskCategoryColor.setOnClickListener(this)
        binding.buttonAddTaskCategory.setOnClickListener(this)


        return binding.root
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length!! <= 100) {
            val categoryNameCharacters = getString(R.string.max_task_category_name_characters, s.length)
            binding.textLengthAddTaskCategoryCharacters.text = categoryNameCharacters
        }

        if (s.length == 100) {
            Toast.makeText(requireContext(), getString(R.string.toast_achieved_max_task_category_name_characters), Toast.LENGTH_SHORT).show()
        }
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_add_task_category_color -> {
                MaterialColorPickerDialog
                    .Builder(requireContext())
                    .setTitle(getString(R.string.description_choose_color))
                    .setColorRes(resources.getIntArray(R.array.themeColors).toList())
                    .setColorListener { _, colorHex ->
                        standardColor = colorHex
                        DrawableCompat.setTint(binding.textAddTaskCategoryColor.background, Color.parseColor(standardColor))
                    }
                    .show()
            }
            R.id.button_add_task_category -> {
                checkEditText()
            }
        }
    }

    private fun checkEditText() = if (binding.editAddTaskCategory.text.toString().isNotEmpty()) {
        Toast.makeText(requireContext(), getString(R.string.toast_category_added), Toast.LENGTH_SHORT).show()
        addTaskCategory()
    } else {
        binding.editAddTaskCategory.error = getString(R.string.hint_enter_category_name)
    }

    private fun addTaskCategory() {
        val addTaskCategories = TaskCategories(
            0,
            binding.editAddTaskCategory.text.toString().trim(),
            standardColor,
            standardIntermediate,
            MainOther().currentTime,
            MainOther().currentTime
        )
        tasksCategoryViewModel.insertTaskCategory(addTaskCategories)
        dialog?.dismiss()
    }
}