package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetToolsTaskBinding
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class ToolsTaskItemBottomSheet : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener,
    CompoundButton.OnCheckedChangeListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetToolsTaskBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties

    private var textSizeValues: List<Int> = (16..34).toList().filter { it % 2 == 0 }

    companion object {
        const val ARG_TOOLS_TASK_ITEM_BS = "arg_tools_task_item_bs"
        const val ARG_TASK_ITEM_TEXT_SIZE_PROGRESS = "arg_task_item_text_size_progress"
        const val ARG_TASK_ITEM_TEXT_SIZE = "arg_task_item_text_size"
        const val ARG_TASK_ITEM_ADD_DATE_TIME = "arg_task_item_add_date_time"
        const val ARG_TASK_ITEM_CHANGE_DATE_TIME = "arg_task_item_change_date_time"
        const val ARG_TASK_ITEM_EXECUTION_DATE_TIME = "arg_task_item_execution_date_time"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_tools_task, container, false)

        retainInstance = true

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        val lastTaskTextSizeProgress = sharedLocalPreferences.getIntValue(ARG_TASK_ITEM_TEXT_SIZE_PROGRESS, 1)
        val lastTaskTextSize = sharedLocalPreferences.getIntValue(ARG_TASK_ITEM_TEXT_SIZE, 18)
        val lastTaskAddDateTimeSwitch = sharedLocalPreferences.getBooleanValue(ARG_TASK_ITEM_ADD_DATE_TIME, false)
        val lastTaskChangeDateTimeSwitch = sharedLocalPreferences.getBooleanValue(ARG_TASK_ITEM_CHANGE_DATE_TIME, false)
        val lastTaskExecutionDateTimeSwitch = sharedLocalPreferences.getBooleanValue(ARG_TASK_ITEM_EXECUTION_DATE_TIME, false)

        binding.apply {
            seekBarTaskTextSize.progress = lastTaskTextSizeProgress
            textViewTaskTextSizeCount.text = lastTaskTextSize.toString()
            switchAddTaskTime.isChecked = lastTaskAddDateTimeSwitch
            switchChangeTaskTime.isChecked = lastTaskChangeDateTimeSwitch
            switchExecutionTaskTime.isChecked = lastTaskExecutionDateTimeSwitch
        }

        binding.seekBarTaskTextSize.setOnSeekBarChangeListener(this)
        binding.switchAddTaskTime.setOnCheckedChangeListener(this)
        binding.switchChangeTaskTime.setOnCheckedChangeListener(this)
        binding.switchExecutionTaskTime.setOnCheckedChangeListener(this)

        return binding.root
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.seek_bar_task_text_size -> {
                sharedLocalPreferences.saveIntValue(ARG_TASK_ITEM_TEXT_SIZE_PROGRESS, progress)
                sharedLocalPreferences.saveIntValue(ARG_TASK_ITEM_TEXT_SIZE, textSizeValues[progress])
                binding.textViewTaskTextSizeCount.text = textSizeValues[progress].toString()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switch_add_task_time -> {
                sharedLocalPreferences.saveBooleanValue(ARG_TASK_ITEM_ADD_DATE_TIME, isChecked)
            }
            R.id.switch_change_task_time -> {
                sharedLocalPreferences.saveBooleanValue(ARG_TASK_ITEM_CHANGE_DATE_TIME, isChecked)
            }
            R.id.switch_execution_task_time -> {
                sharedLocalPreferences.saveBooleanValue(ARG_TASK_ITEM_EXECUTION_DATE_TIME, isChecked)
            }
        }
    }
}