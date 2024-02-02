package com.dikiyserge.employees.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Department
import com.dikiyserge.employees.data.EmployeeItem
import com.dikiyserge.employees.databinding.FragmentMainBinding
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import com.dikiyserge.employees.viewmodel.SortType
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainFragment : Fragment(), OnTabSelectedListener {
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(Repository(requireActivity().applicationContext))
    }

    private var _binding: FragmentMainBinding? = null

    private val binding
        get() = _binding!!

    private lateinit var departments: List<Department>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tabLayout.addOnTabSelectedListener(this)

        viewModel.departmentsLiveData.observe(viewLifecycleOwner) { dep ->
            departments = dep

            for ((index, department) in departments.withIndex()) {
                val tab = binding.tabLayout.newTab()
                tab.text = department.name
                tab.id = index
                binding.tabLayout.addTab(tab)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.employeeItemsLiveData.observe(viewLifecycleOwner) { employeeItems ->
            binding.recyclerView.adapter = EmployeeRecyclerAdapter(employeeItems)
        }

        if (savedInstanceState == null) {
            viewModel.loadEmployees()
        }

        binding.editTextFilter.addTextChangedListener { text ->
            viewModel.filter = text?.toString()?.ifEmpty { null }
        }

        binding.imageButtonSorting.setOnClickListener {
            SortingListDialogFragment.newInstance(viewModel.sorting).show(parentFragmentManager, null)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
            if (tab.id in departments.indices) {
                val department = departments[tab.id]
                viewModel.department = department.code
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        //
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}