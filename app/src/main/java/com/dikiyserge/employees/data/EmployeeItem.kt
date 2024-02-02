package com.dikiyserge.employees.data

enum class EmployeeItemType {
    EMPTY, EMPLOYEE, EMPLOYEE_DATE, DATE_SEPARATOR
}

data class EmployeeItem(val itemType: EmployeeItemType, val employee: Employee? = null, val date: String? = null)