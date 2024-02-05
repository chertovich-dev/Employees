package com.dikiyserge.employees.model.net

import com.dikiyserge.employees.data.Employees
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url

interface EmployeesService {
    @Headers(
        "Content-Type: application/json",
        "Prefer: code=200, dynamic=false"
    )
    @GET("mocks/kode-education/trainee-test/25143926/users")
    suspend fun getEmployees(): Response<Employees>
}