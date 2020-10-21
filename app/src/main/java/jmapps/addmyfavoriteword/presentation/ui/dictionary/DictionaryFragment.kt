package jmapps.addmyfavoriteword.presentation.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentDictionaryBinding
import jmapps.addmyfavoriteword.presentation.mvp.other.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.other.OtherPresenter

class DictionaryFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var otherPresenter: OtherPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dictionary, container, false)

        otherPresenter = OtherPresenter(this)
        otherPresenter.defaultState()
        //otherPresenter.updateState(testList)

        dictionaryViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        return binding.root
    }

    override fun defaultState() {
        binding.rvDictionaryCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvDictionaryCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }
}