package com.dikiyserge.employees.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dikiyserge.employees.R
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainFragment : Fragment(), OnTabSelectedListener {
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(Repository(requireActivity().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(this)

        viewModel.departmentsLiveData.observe(viewLifecycleOwner) { departments ->
            for (department in departments) {
                val tab = tabLayout.newTab()
                tab.text = department.name
                tabLayout.addTab(tab)
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        viewModel.employeeItemsLiveData.observe(viewLifecycleOwner) { employeeItems ->
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = EmployeeRecyclerAdapter(employeeItems)

        }

        if (savedInstanceState == null) {
            viewModel.loadEmployees()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        //
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        //
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        //
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