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
import jmapps.addmyfavoriteword.databinding.BottomsheetToolsWordBinding
import jmapps.addmyfavoriteword.presentation.ui.preferences.SharedLocalProperties

class ToolsWordItemBottomSheet : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener,
    CompoundButton.OnCheckedChangeListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetToolsWordBinding

    private lateinit var preferences: SharedPreferences
    private lateinit var sharedLocalPreferences: SharedLocalProperties

    private var textSizeValues: List<Int> = (16..34).toList().filter { it % 2 == 0 }

    companion object {
        const val ARG_TOOLS_WORD_ITEM_BS = "arg_tools_word_item_bs"
        const val ARG_WORD_GRID_COUNT = "arg_word_grid_count"
        const val ARG_WORDS_TEXT_SIZE = "arg_words_text_size"
        const val ARG_WORDS_TEXT_SIZE_PROGRESS = "arg_words_text_size_progress"
        const val ARG_WORD_STATE = "arg_word_state"
        const val ARG_WORD_TRANSCRIPTION_STATE = "arg_word_transcription_state"
        const val ARG_WORD_TRANSLATE_STATE = "arg_word_translate_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_tools_word, container, false)

        retainInstance = true

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedLocalPreferences = SharedLocalProperties(preferences)

        val lastWordGridValueProgress = sharedLocalPreferences.getIntValue(ARG_WORD_GRID_COUNT, 2)
        val lastWordTextSizeValueProgress = sharedLocalPreferences.getIntValue(ARG_WORDS_TEXT_SIZE_PROGRESS, 1)
        val lastWordState = sharedLocalPreferences.getBooleanValue(ARG_WORD_STATE, true)
        val lastWordTranscriptionState = sharedLocalPreferences.getBooleanValue(ARG_WORD_TRANSCRIPTION_STATE, true)
        val lastWordTranslateState = sharedLocalPreferences.getBooleanValue(ARG_WORD_TRANSLATE_STATE, true)

        binding.apply {
            seekBarWordGrinCount.progress = lastWordGridValueProgress!!
            textGridCount.text = (lastWordGridValueProgress + 1).toString()
            seekBarWordTextSize.progress = lastWordTextSizeValueProgress!!
            textTextSizeCount.text = textSizeValues[lastWordTextSizeValueProgress].toString()
            switchShowWord.isChecked = lastWordState!!
            switchShowWordTranscription.isChecked = lastWordTranscriptionState!!
            switchShowWordTranslate.isChecked = lastWordTranslateState!!
        }

        binding.seekBarWordGrinCount.setOnSeekBarChangeListener(this)
        binding.seekBarWordTextSize.setOnSeekBarChangeListener(this)
        binding.switchShowWord.setOnCheckedChangeListener(this)
        binding.switchShowWordTranscription.setOnCheckedChangeListener(this)
        binding.switchShowWordTranslate.setOnCheckedChangeListener(this)

        return binding.root
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.seek_bar_word_grin_count -> {
                binding.textGridCount.text = (progress + 1).toString()
                sharedLocalPreferences.saveIntValue(ARG_WORD_GRID_COUNT, progress)
            }
            R.id.seek_bar_word_text_size -> {
                binding.textTextSizeCount.text = textSizeValues[progress].toString()
                sharedLocalPreferences.saveIntValue(ARG_WORDS_TEXT_SIZE_PROGRESS, progress)
                sharedLocalPreferences.saveIntValue(ARG_WORDS_TEXT_SIZE, textSizeValues[progress])
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switch_show_word -> {
                sharedLocalPreferences.saveBooleanValue(ARG_WORD_STATE, isChecked)
                if (!isChecked) {
                    if (!binding.switchShowWordTranslate.isChecked) {
                        binding.switchShowWordTranslate.isChecked = true
                    }
                }
            }
            R.id.switch_show_word_transcription -> {
                sharedLocalPreferences.saveBooleanValue(ARG_WORD_TRANSCRIPTION_STATE, isChecked)
            }
            R.id.switch_show_word_translate -> {
                sharedLocalPreferences.saveBooleanValue(ARG_WORD_TRANSLATE_STATE, isChecked)
                if (!isChecked) {
                    if (!binding.switchShowWord.isChecked) {
                        binding.switchShowWord.isChecked = true
                    }
                }
            }
        }
    }
}