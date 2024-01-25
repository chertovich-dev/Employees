package com.dikiyserge.employees.model

import android.content.Context
import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Department
import com.dikiyserge.employees.data.Employees
import com.dikiyserge.employees.model.net.EmployeesService
import com.dikiyserge.employees.model.net.NetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.min

class Repository(private val appContext: Context) {
    fun getDepartments(): List<Department> {
        val departmentNames = appContext.resources.getStringArray(R.array.department_names)
        val departmentCodes = appContext.resources.getStringArray(R.array.department_codes)

        val count = min(departmentNames.size, departmentCodes.size)

        val departments = mutableListOf<Department>()

        for (i in 0 until count) {
            val department = Department(departmentNames[i], departmentCodes[i])
            departments.add(department)
        }

        return departments
    }

    suspend fun getEmployees(): Employees = withContext(Dispatchers.IO) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://stoplight.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val employeesService = retrofit.create(EmployeesService::class.java)
        val response = employeesService.getEmployees()

        if (response.isSuccessful) {
            return@withContext response?.body() ?: throw Exception("body is null")
        } else {
            throw NetException(response.code())
        }
    }
}