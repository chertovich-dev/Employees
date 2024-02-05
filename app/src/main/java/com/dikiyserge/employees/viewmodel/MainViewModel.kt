package com.dikiyserge.employees.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dikiyserge.employees.data.DEPARTMENT_ALL
import com.dikiyserge.employees.data.Department
import com.dikiyserge.employees.data.Employee
import com.dikiyserge.employees.data.EmployeeItem
import com.dikiyserge.employees.data.EmployeeItemType
import com.dikiyserge.employees.data.Employees
import com.dikiyserge.employees.model.Repository
import com.dikiyserge.employees.model.net.NetException
import com.dikiyserge.employees.view.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

private const val EMPLOYEE_START_ITEM_COUNT = 10
private const val ERROR_DELAY = 2000L

enum class SortType {
    ALPHABET, BIRTHDAY
}

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val departments = repository.getDepartments()

    var departmentsLiveData: LiveData<List<Department>> = MutableLiveData(departments)

    private var employees = Employees(listOf())

    private val employeeItems = mutableListOf<EmployeeItem>()

    private val _employeeItemsLiveData: MutableLiveData<List<EmployeeItem>> = MutableLiveData(employeeItems)
    val employeeItemsLiveData: LiveData<List<EmployeeItem>> = _employeeItemsLiveData

    init {
        repeat(EMPLOYEE_START_ITEM_COUNT) {
            employeeItems.add(EmployeeItem(EmployeeItemType.EMPTY))
        }
    }

    var department: String = DEPARTMENT_ALL
        set(value) {
            if (value != field) {
                field = value
                loadEmployeeItems()
            }
        }

    var filter: String? = null
        set(value) {
            if (value != field) {
                field = value
                loadEmployeeItems()
            }
        }

    var sorting = SortType.ALPHABET
        set(value) {
            if (value != field) {
                field = value
                loadEmployeeItems()
            }
        }

    private var nav: Nav? = null
        set(value) {
            field = value
            _navLiveData.value = field
        }

    private val _navLiveData = MutableLiveData(nav)
    val navLiveData: LiveData<Nav?> = _navLiveData

    private fun getEmployeeItemsOrderedByNames(employees: List<Employee>): List<EmployeeItem> {
        val employeeItems = mutableListOf<EmployeeItem>()

        val sortedEmployees = employees.sortedWith { employee1, employee2 -> employee1.name.compareTo(employee2.name) }

        for (employee in sortedEmployees) {
            val employeeItem = EmployeeItem(EmployeeItemType.EMPLOYEE, employee)
            employeeItems.add(employeeItem)
        }

        return employeeItems
    }

    private fun getEmployeeItemsOrderedByBirthday(employees: List<Employee>): List<EmployeeItem> {
        val employeeItems = mutableListOf<EmployeeItem>()

        val sortedEmployees = employees.sortedWith { employee1, employee2 ->
            val monthComp = employee1.birthdayDate.month.compareTo(employee2.birthdayDate.month)

            if (monthComp == 0) {
                employee1.birthdayDate.dayOfMonth.compareTo(employee2.birthdayDate.dayOfMonth)
            } else {
                monthComp
            }
        }

        val employeesBirthdayInThisYear = mutableListOf<Employee>()
        val employeesBirthdayInNextYear = mutableListOf<Employee>()

        val today = LocalDate.now()

        for (employee in sortedEmployees) {
            val birthdayInThisYear = LocalDate.of(today.year, employee.birthdayDate.month,
                employee.birthdayDate.dayOfMonth)

            if (birthdayInThisYear < today) {
                employeesBirthdayInNextYear.add(employee)
            } else {
                employeesBirthdayInThisYear.add(employee)
            }
        }

        for (employee in employeesBirthdayInThisYear) {
            val employeeItem = EmployeeItem(EmployeeItemType.EMPLOYEE_DATE, employee)
            employeeItems.add(employeeItem)
        }

        val nextYear = "${today.year + 1}"

        if (employeesBirthdayInNextYear.isNotEmpty()) {
            val employeeItem = EmployeeItem(EmployeeItemType.DATE_SEPARATOR, null, nextYear)
            employeeItems.add(employeeItem)
        }

        for (employee in employeesBirthdayInNextYear) {
            val employeeItem = EmployeeItem(EmployeeItemType.EMPLOYEE_DATE, employee)
            employeeItems.add(employeeItem)
        }

        return employeeItems
    }

    private fun isNecessaryEmployeeByDepartment(employee: Employee): Boolean {
        return if (department == DEPARTMENT_ALL) {
            true
        } else {
            employee.department == department
        }
    }

    private fun isNecessaryEmployeeByFilter(employee: Employee): Boolean {
        val filterValue = filter

        return if (filterValue == null) {
            true
        } else {
            employee.firstName.contains(filterValue, true) || employee.lastName.contains(filterValue, true) ||
                    employee.userTag.contains(filterValue, true)
        }
    }

    fun loadEmployeeItems() {
        val items = mutableListOf<Employee>()

        for (employee in employees.items) {
            if (isNecessaryEmployeeByDepartment(employee) && isNecessaryEmployeeByFilter(employee)) {
                items.add(employee)
            }
        }

        employeeItems.clear()

        when (sorting) {
            SortType.ALPHABET -> employeeItems.addAll(getEmployeeItemsOrderedByNames(items))
            SortType.BIRTHDAY -> employeeItems.addAll(getEmployeeItemsOrderedByBirthday(items))
        }

        _employeeItemsLiveData.value = employeeItems
    }

    fun loadEmployees(showError: Boolean = false) {
        viewModelScope.launch {
            val isException: Boolean = try {
                employees = repository.getEmployees()
                loadEmployeeItems()
                false
            }
            catch (e: NetException) {

                log("NetException")
                true
            }
            catch (e: UnknownHostException) {

                log("UnknownHostException")
                true
            }
            catch (e: Exception) {
                log("Exception ${e.toString()}")

                true
            }

            if (isException && showError) {
                delay(ERROR_DELAY)
                nav = MainToErrorNav()
            }
        }
    }

    fun tryAgain() {
        nav = ErrorToMainNav()
    }

    fun selectEmployee(employee: Employee) {
        nav = MainToEmployee(employee)
    }

    fun backToMain() {
        nav = EmployeeToMain()
    }
}