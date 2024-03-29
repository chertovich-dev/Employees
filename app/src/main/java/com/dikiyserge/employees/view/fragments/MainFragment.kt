package com.dikiyserge.employees.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Department
import com.dikiyserge.employees.data.Employee
import com.dikiyserge.employees.databinding.FragmentMainBinding
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.view.EmployeeRecyclerAdapter
import com.dikiyserge.employees.view.OnEmployeeListener
import com.dikiyserge.employees.view.log
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import com.dikiyserge.employees.viewmodel.Message
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainFragment : Fragment(), OnTabSelectedListener, OnEmployeeListener {
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
            binding.recyclerView.adapter = EmployeeRecyclerAdapter(employeeItems, this)

            binding.swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadEmployees()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        binding.editTextFilter.addTextChangedListener { text ->
            viewModel.filter = text?.toString()?.ifEmpty { null }
        }

        binding.imageButtonSorting.setOnClickListener {
            SortingListDialogFragment.newInstance(viewModel.sorting).show(parentFragmentManager, null)
        }

        viewModel.messageLiveData.observe(viewLifecycleOwner) { message ->
            val messageResId = when (message) {
                Message.Loading -> {
                    R.string.loading
                }

                Message.NoNetwork -> {
                    R.string.error_no_network
                }

                Message.Error -> {
                    R.string.error
                }
            }

            val messageText = getString(messageResId)

            val colorResId = if (message == Message.Loading) {
                 R.color.blue
            } else {
                R.color.red
            }

            val color = getColor(resources, colorResId, null)

            Snackbar.make(binding.recyclerView, messageText, Snackbar.LENGTH_LONG).setBackgroundTint(color).show()
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

    /** OnEmployeeListener */
    override fun onSelectEmployee(employee: Employee) {
        viewModel.selectEmployee(employee)
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