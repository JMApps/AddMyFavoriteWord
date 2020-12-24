package jmapps.addmyfavoriteword.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FlipWordBackBinding
import jmapps.addmyfavoriteword.databinding.FlipWordFrontBinding
import jmapps.addmyfavoriteword.presentation.ui.models.WordsItemViewModel

class FlipWordContainerFragment : Fragment() {

    private lateinit var bindingFront: FlipWordFrontBinding
    private lateinit var bindingBack: FlipWordBackBinding
    private lateinit var wordsItemViewModel: WordsItemViewModel

    private var sectionNumber: Int? = null
    private var flipModeState: Boolean? = null

    companion object {
        private const val ARG_FLIP_WORD_SECTION_NUMBER = "arg_flip_word_section_number"
        private const val ARG_FLIP_WORD_MODE_STATE = "arg_flip_word_mode_state"

        @JvmStatic
        fun newInstance(sectionNumber: Int, flipModeState: Boolean): FlipWordContainerFragment {
            return FlipWordContainerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FLIP_WORD_SECTION_NUMBER, sectionNumber)
                    putBoolean(ARG_FLIP_WORD_MODE_STATE, flipModeState)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wordsItemViewModel = ViewModelProvider(this).get(WordsItemViewModel::class.java)
        sectionNumber = arguments?.getInt(ARG_FLIP_WORD_SECTION_NUMBER)
        flipModeState = arguments?.getBoolean(ARG_FLIP_WORD_MODE_STATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        retainInstance = true

        return if (flipModeState!!) {
            bindingFront = DataBindingUtil.inflate(inflater, R.layout.flip_word_front, container, false)
            initFlipFront()
            bindingFront.flViewWords.setOnClickListener {
                bindingFront.flViewWords.flipTheView()
            }
            bindingFront.root
        } else {
            bindingBack = DataBindingUtil.inflate(inflater, R.layout.flip_word_back, container, false)
            initFlipBack()
            bindingBack.flViewWords.setOnClickListener {
                bindingBack.flViewWords.flipTheView()
            }
            bindingBack.root
        }
    }

    private fun initFlipFront() {
        wordsItemViewModel.getAllWordsList(sectionNumber!!.toLong(), "").observe(viewLifecycleOwner, {

        })
    }

    private fun initFlipBack() {
        wordsItemViewModel.getAllWordsList(sectionNumber!!.toLong(), "").observe(viewLifecycleOwner, {

        })
    }
}