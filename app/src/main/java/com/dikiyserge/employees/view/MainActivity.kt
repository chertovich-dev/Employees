package com.dikiyserge.employees.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dikiyserge.employees.R
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.view.fragments.KEY_EMPLOYEE
import com.dikiyserge.employees.viewmodel.EmployeeToMain
import com.dikiyserge.employees.viewmodel.ErrorToMainNav
import com.dikiyserge.employees.viewmodel.MainToEmployee
import com.dikiyserge.employees.viewmodel.MainToErrorNav

private const val LOG_TAG = "_test"

fun log(text: String) = Log.i(LOG_TAG, text)

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(Repository(applicationContext)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.navLiveData.observe(this) { nav ->
            val navController = findNavController( R.id.fragmentContainerView)

            log("!")
            when (nav) {
                null -> {
                    //
                }

                is MainToErrorNav -> {
                    navController.navigate(R.id.action_mainFragment_to_errorFragment)
                }

                is ErrorToMainNav -> {
                    navController.navigate(R.id.action_errorFragment_to_mainFragment)
                }

                is MainToEmployee -> {
                    val bundle = Bundle()
                    bundle.putParcelable(KEY_EMPLOYEE, nav.employee)
                    navController.navigate(R.id.action_mainFragment_to_employeeFragment, bundle)
                }

                is EmployeeToMain -> {
                    navController.navigate(R.id.action_employeeFragment_to_mainFragment)
                }
            }
        }
    }
}