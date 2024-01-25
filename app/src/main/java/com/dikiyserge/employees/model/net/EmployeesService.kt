package com.dikiyserge.employees.model.net

import com.dikiyserge.employees.data.Employees
import retrofit2.Response
import retrofit2.http.GET

interface EmployeesService {
    @GET("mocks/kode-education/trainee-test/25143926/users")
    suspend fun getEmployees(): Response<Employees>
}