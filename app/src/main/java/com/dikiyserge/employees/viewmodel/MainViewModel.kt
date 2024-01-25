package com.dikiyserge.employees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dikiyserge.employees.data.Department
import com.dikiyserge.employees.data.EmployeeItem
import com.dikiyserge.employees.data.EmployeeItemType
import com.dikiyserge.employees.data.Employees
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.model.net.NetException
import com.dikiyserge.employees.view.log
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private const val EMPLOYEE_START_ITEM_COUNT = 10

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val departments = repository.getDepartments()
    val departmentsLiveData: LiveData<List<Department>> = MutableLiveData(departments)

    private var employees = Employees(listOf())

    private val employeeItems = mutableListOf<EmployeeItem>()
    val employeeItemsLiveData: LiveData<List<EmployeeItem>> = MutableLiveData(employeeItems)

    init {
        repeat(EMPLOYEE_START_ITEM_COUNT) {
            employeeItems.add(EmployeeItem(EmployeeItemType.EMPTY))
        }
    }

    fun loadEmployees() {
        viewModelScope.launch {
            try {
                employees = repository.getEmployees()

            }
            catch (e: NetException) {
                log("NetException")
            }
            catch (e: UnknownHostException) {
                log("UnknownHostException")
            }
            catch (e: Exception) {

                log("Exception ${e.toString()}")
            }
        }

    }

    fun loadEmployeeItems() {
        //
    }
}