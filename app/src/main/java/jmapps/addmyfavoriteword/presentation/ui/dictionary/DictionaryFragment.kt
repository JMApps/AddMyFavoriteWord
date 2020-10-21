package jmapps.addmyfavoriteword.presentation.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentDictionaryBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter

class DictionaryFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var binding: FragmentDictionaryBinding
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dictionaryViewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dictionary, container, false)

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.defaultState()
        //otherPresenter.updateState(testList)

        return binding.root
    }

    override fun initView() {

    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddDictionaryCategory.startAnimation(show)

        binding.rvDictionaryCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvDictionaryCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }
}