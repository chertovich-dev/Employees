package com.dikiyserge.employees.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dikiyserge.employees.R
import com.dikiyserge.employees.viewmodel.MainViewModel
import com.dikiyserge.employees.viewmodel.MainViewModelFactory
import androidx.activity.viewModels
import com.dikiyserge.employees.model.Repository

private const val LOG_TAG = "_test"

fun log(text: String) = Log.i(LOG_TAG, text)

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(Repository(applicationContext)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}