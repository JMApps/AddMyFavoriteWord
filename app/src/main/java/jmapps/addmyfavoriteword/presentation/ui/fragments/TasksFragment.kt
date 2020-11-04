package jmapps.addmyfavoriteword.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.addmyfavoriteword.R
import jmapps.addmyfavoriteword.databinding.FragmentTasksBinding
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.ContractInterface
import jmapps.addmyfavoriteword.presentation.mvp.otherFragments.OtherFragmentsPresenter
import jmapps.addmyfavoriteword.presentation.ui.adapter.TaskCategoriesAdapter
import jmapps.addmyfavoriteword.presentation.ui.bottomsheets.AddTaskCategory
import jmapps.addmyfavoriteword.presentation.ui.model.TasksViewModel

class TasksFragment : Fragment(), ContractInterface.OtherView,
    TaskCategoriesAdapter.OnItemClickTaskCategory, View.OnClickListener {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var binding: FragmentTasksBinding
    private lateinit var otherFragmentsPresenter: OtherFragmentsPresenter

    private lateinit var taskCategoriesAdapter: TaskCategoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false)

        otherFragmentsPresenter = OtherFragmentsPresenter(this)
        otherFragmentsPresenter.initView()
        otherFragmentsPresenter.defaultState()

        binding.rvTaskCategories.addOnScrollListener(onAddScroll)
        binding.fabAddTaskCategory.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        val addTaskCategory = AddTaskCategory()
        addTaskCategory.show(childFragmentManager, "tagg")
    }

    override fun initView() {
        tasksViewModel.allTaskCategories.observe(this, Observer { taskCategoriesList ->
            taskCategoriesList.let {
                taskCategoriesAdapter = TaskCategoriesAdapter(requireContext(), taskCategoriesList, this)
                val verticalLayout = LinearLayoutManager(requireContext())
                binding.rvTaskCategories.layoutManager = verticalLayout
                binding.rvTaskCategories.adapter = taskCategoriesAdapter
                otherFragmentsPresenter.updateState(taskCategoriesList)

            }
        })
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

    override fun onItemClickTaskCategory(_id: Long) {
        Toast.makeText(requireContext(), "Id = $_id", Toast.LENGTH_SHORT).show()
    }

    private val onAddScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                binding.fabAddTaskCategory.hide()
            } else {
                binding.fabAddTaskCategory.show()
            }
        }
    }
}