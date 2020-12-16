package jmapps.addmyfavoriteword.presentation.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.BottomsheetRenameWordBinding
import jmapps.addmyfavoriteword.presentation.ui.models.WordsItemViewModel
import jmapps.addmyfavoriteword.presentation.ui.other.MainOther

class RenameWordItemBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    override fun getTheme() = R.style.BottomSheetStyleFull

    private lateinit var binding: BottomsheetRenameWordBinding
    private lateinit var wordsItemViewModel: WordsItemViewModel

    private var wordItemId: Long? = null
    private var word: String? = null
    private var wordTranscription: String? = null
    private var wordTranslate: String? = null

    companion object {
        const val ARG_RENAME_WORD_ITEM_BS = "arg_rename_word_item_bs"
        private const val ARG_RENAME_WORD_ITEM_ID = "arg_rename_word_item_id"
        private const val ARG_RENAME_WORD = "arg_rename_word"
        private const val ARG_RENAME_WORD_TRANSCRIPTION = "arg_rename_word_transcription"
        private const val ARG_RENAME_WORD_TRANSLATE = "arg_rename_word_translate"

        @JvmStatic
        fun toInstance(
            wordItemId: Long,
            word: String,
            wordTranscription: String,
            wordTranslate: String): RenameWordItemBottomSheet {
            return RenameWordItemBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_RENAME_WORD_ITEM_ID, wordItemId)
                    putString(ARG_RENAME_WORD, word)
                    putString(ARG_RENAME_WORD_TRANSCRIPTION, wordTranscription)
                    putString(ARG_RENAME_WORD_TRANSLATE, wordTranslate)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordsItemViewModel = ViewModelProvider(this).get(WordsItemViewModel::class.java)

        wordItemId = arguments?.getLong(ARG_RENAME_WORD_ITEM_ID)
        word = arguments?.getString(ARG_RENAME_WORD)
        wordTranscription = arguments?.getString(ARG_RENAME_WORD_TRANSCRIPTION)
        wordTranslate = arguments?.getString(ARG_RENAME_WORD_TRANSLATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_rename_word, container, false)

        binding.editChangeWord.setText(word!!)
        binding.editChangeWord.setSelection(word!!.length)
        binding.editChangeWordTranscription.setText(wordTranscription!!)
        binding.editChangeWordTranscription.setSelection(wordTranscription!!.length)
        binding.editChangeWordTranslate.setText(wordTranslate!!)
        binding.editChangeWordTranslate.setSelection(wordTranslate!!.length)

        binding.buttonChangeWordAndClose.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        checkEditTexts()
    }

    private fun checkEditTexts() {
        when {
            binding.editChangeWord.text!!.isNotEmpty() && binding.editChangeWordTranslate.text!!.isNotEmpty() -> {
                if (word!! != binding.editChangeWord.text.toString() ||
                    wordTranscription!! != binding.editChangeWordTranscription.text!!.toString() ||
                    wordTranslate!! != binding.editChangeWordTranslate.text.toString()) {
                    renameWord()
                } else {
                    dialog?.dismiss()
                }
            }
            binding.editChangeWord.text!!.isEmpty() -> {
                binding.editChangeWord.error = getString(R.string.hint_enter_new_word)
            }
            binding.editChangeWordTranslate.text!!.isEmpty() -> {
                binding.editChangeWordTranslate.error = getString(R.string.hint_enter_word_translate)
            }
        }
    }

    private fun renameWord() {
        wordsItemViewModel.updateWordItem(
            binding.editChangeWord.text.toString(),
            binding.editChangeWordTranscription.text.toString(),
            binding.editChangeWordTranslate.text.toString(),
            MainOther().currentTime,
            wordItemId!!
        )
        Toast.makeText(requireContext(), getString(R.string.toast_word_changed), Toast.LENGTH_SHORT).show()
        dialog?.dismiss()
    }
}