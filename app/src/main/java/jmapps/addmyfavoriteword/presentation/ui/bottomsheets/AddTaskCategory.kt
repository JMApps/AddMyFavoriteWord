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
import jmapps.addmyfavoriteword.data.database.room.tasks.TaskCategories
import jmapps.addmyfavoriteword.databinding.BottomsheetAddTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.model.TasksViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther
import kotlinx.android.synthetic.main.bottomsheet_add_task_category.*

class AddTaskCategory : BottomSheetDialogFragment() {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var binding: BottomsheetAddTaskCategoryBinding
    private var standardColor: String = "#9E9E9E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_task_category, container, false)

        binding.imageButtonCurrentCategoryColor.setBackgroundColor(Color.parseColor(standardColor))
        binding.imageButtonCurrentCategoryColor.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(requireContext())
                .setColorRes(resources.getIntArray(R.array.themeColors).toList())
                .setColorListener { color, colorHex ->
                    Toast.makeText(requireContext(), "Color = $colorHex", Toast.LENGTH_SHORT).show()
                    standardColor = colorHex
                    binding.imageButtonCurrentCategoryColor.setBackgroundColor(Color.parseColor(standardColor))
                }
                .show()
        }

        binding.buttonAddTaskCategory.setOnClickListener {
            val addTaskCategories = TaskCategories(
                0,
                edit_add_task_category.text.toString(),
                standardColor,
                MainOther().currentTime,
                MainOther().currentTime)
            tasksViewModel.insertTaskCategory(addTaskCategories)
            dialog?.dismiss()
        }

        return binding.root
    }
}
