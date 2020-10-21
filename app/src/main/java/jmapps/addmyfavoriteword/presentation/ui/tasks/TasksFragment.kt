package jmapps.addmyfavoriteword.presentation.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentTasksBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter

class TasksFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.defaultState()
        //otherPresenter.updateState(testList)

        tasksViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        return binding.root
    }

    override fun defaultState() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.show);
        binding.fabAddTaskCategory.startAnimation(show)

        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvTaskCategories.visibility = otherFragmentsPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherFragmentsPresenter.descriptionMain()
    }
}