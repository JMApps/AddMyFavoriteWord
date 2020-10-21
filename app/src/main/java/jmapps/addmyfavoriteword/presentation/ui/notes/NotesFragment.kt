package jmapps.addmyfavoriteword.presentation.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentNotesBinding
import jmapps.addmyfavoriteword.presentation.mvp.other.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.other.OtherPresenter

class NotesFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentNotesBinding
    private lateinit var otherPresenter: OtherPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false)

        otherPresenter = OtherPresenter(this)
        otherPresenter.defaultState()
        //otherPresenter.updateState(testList)

        notesViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        return binding.root
    }

    override fun defaultState() {
        binding.rvNoteCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvNoteCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }
}