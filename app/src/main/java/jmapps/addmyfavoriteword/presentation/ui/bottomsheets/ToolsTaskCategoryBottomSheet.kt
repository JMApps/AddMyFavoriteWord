package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetToolsTaskCategoryBinding
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class ToolsTaskCategoryBottomSheet : BottomSheetDialogFragment(),
    CompoundButton.OnCheckedChangeListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetToolsTaskCategoryBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties

    companion object {
        const val ARG_TOOLS_TASK_ITEM_BS = "arg_tools_task_category_bs"
        const val KEY_TASK_CATEGORY_ADD_DATE_TIME = "key_task_category_add_date_time"
        const val KEY_TASK_CATEGORY_CHANGE_DATE_TIME = "key_task_category_change_date_time"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_tools_task_category, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        val lastTaskCategoryAddDateTimeSwitch = sharedLocalPreferences.getBooleanValue(KEY_TASK_CATEGORY_ADD_DATE_TIME, false)
        val lastTaskCategoryChangeDateTimeSwitch = sharedLocalPreferences.getBooleanValue(KEY_TASK_CATEGORY_CHANGE_DATE_TIME, false)

        binding.apply {
            switchAddTaskCategoryTime.isChecked = lastTaskCategoryAddDateTimeSwitch!!
            switchChangeTaskCategoryTime.isChecked = lastTaskCategoryChangeDateTimeSwitch!!
        }

        binding.switchAddTaskCategoryTime.setOnCheckedChangeListener(this)
        binding.switchChangeTaskCategoryTime.setOnCheckedChangeListener(this)

        return binding.root
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switch_add_task_category_time -> {
                sharedLocalPreferences.saveBooleanValue(KEY_TASK_CATEGORY_ADD_DATE_TIME, isChecked)
            }
            R.id.switch_change_task_category_time -> {
                sharedLocalPreferences.saveBooleanValue(KEY_TASK_CATEGORY_CHANGE_DATE_TIME, isChecked)
            }
        }
    }
}