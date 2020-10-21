package jmapps.addmyfavoriteword.presentation.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentTasksBinding
import jmapps.addmyfavoriteword.presentation.mvp.other.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.other.OtherPresenter

class TasksFragment : Fragment(), ContractInterface.OtherView {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding
    private lateinit var otherPresenter: OtherPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)

        otherPresenter = OtherPresenter(this)
        otherPresenter.defaultState()
        //otherPresenter.updateState(testList)

        tasksViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        return binding.root
    }

    override fun defaultState() {
        binding.rvTaskCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }

    override fun updateState() {
        binding.rvTaskCategories.visibility = otherPresenter.recyclerCategory()
        binding.textMainViewDescription.visibility = otherPresenter.descriptionMain()
    }
}