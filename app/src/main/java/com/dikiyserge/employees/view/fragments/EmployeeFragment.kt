package com.dikiyserge.employees.view.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Employee
import com.dikiyserge.employees.databinding.FragmentEmployeeBinding
import com.dikiyserge.employees.databinding.FragmentMainBinding
import com.dikiyserge.employees.getParcelableData
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.view.log
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val KEY_EMPLOYEE = "employee"


class EmployeeFragment : Fragment() {
    private var employee: Employee? = null

    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(Repository(requireActivity().applicationContext))
    }

    private var _binding: FragmentEmployeeBinding? = null

    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            employee = it.getParcelableData<Employee>(KEY_EMPLOYEE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.imageViewBack.setOnClickListener {
            viewModel.backToMain()
        }

        employee?.let {
            binding.textViewName.text = it.name
            binding.textViewUserTag.text = it.userTag.lowercase()
            binding.textViewPosition.text = it.position
            val birthday = it.birthdayDate

            if (birthday != null) {
                binding.textViewBirthday.text = birthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

                val now = LocalDate.now()
                val age = java.time.temporal.ChronoUnit.YEARS.between(birthday, now)
                binding.textViewAge.text = "$age ${resources.getString(R.string.years)}"

                binding.textViewPhone.text = it.phone
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(employee: Employee) =
            EmployeeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_EMPLOYEE, employee)
                }
            }
    }
}