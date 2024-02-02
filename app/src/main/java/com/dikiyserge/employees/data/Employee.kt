package com.dikiyserge.employees.data

import com.dikiyserge.employees.view.log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

data class Employee(
    val id: String,
    val avatarUrl: String,
    val firstName: String,
    val lastName: String,
    val userTag: String,
    val department: String,
    val position: String,
    val birthday: String,
    val phone: String
) {
    val birthdayDate: LocalDate
        get() = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val name: String
        get() = "$firstName $lastName"

    override fun toString(): String {
        return "$birthday $name"
    }
}