package com.dikiyserge.employees.viewmodel

import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Employee

sealed class Nav(val action: Int)

class MainToErrorNav() : Nav(R.id.action_mainFragment_to_errorFragment)

class ErrorToMainNav() : Nav(R.id.action_errorFragment_to_mainFragment)

class MainToEmployee(val employee: Employee) : Nav(R.id.action_mainFragment_to_employeeFragment)

class EmployeeToMain() : Nav(R.id.action_employeeFragment_to_mainFragment)