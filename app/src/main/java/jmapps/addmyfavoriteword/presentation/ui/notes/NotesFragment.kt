package jmapps.addmyfavoriteword.presentation.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentNotesBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter

class NotesFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentNotesBinding
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.defaultState()
        //otherPresenter.updateState(testList)

        return binding.root
    }

    override fun initView(sortedBy: String) {

    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddNotesCategory.startAnimation(show)

        binding.rvNoteCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvNoteCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }
}