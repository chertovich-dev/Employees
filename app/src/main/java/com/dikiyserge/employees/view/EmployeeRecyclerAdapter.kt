package com.dikiyserge.employees.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dikiyserge.employees.R
import com.dikiyserge.employees.data.Employee
import com.dikiyserge.employees.data.EmployeeItem
import com.dikiyserge.employees.data.EmployeeItemType
import java.time.format.DateTimeFormatter

private const val EMPTY_NAME = "                                     "
private const val EMPTY_POSITION = "                    "

interface OnEmployeeListener {
    fun onSelectEmployee(employee: Employee)
}

class EmployeeRecyclerAdapter(private val employeeItems: List<EmployeeItem>, private val listener: OnEmployeeListener) :
    RecyclerView.Adapter<EmployeeRecyclerAdapter.EmployeeViewHolder>() {

    private fun setVisibilities(holder: EmployeeViewHolder, employeeItem: EmployeeItem) {
        val employeeVisible = employeeItem.itemType in setOf(EmployeeItemType.EMPLOYEE, EmployeeItemType.EMPLOYEE_DATE)
        val employeeAndEmptyVisible = employeeVisible || employeeItem.itemType == EmployeeItemType.EMPTY

        val birthdayVisible = employeeItem.itemType == EmployeeItemType.EMPLOYEE_DATE
        val yearVisible = employeeItem.itemType == EmployeeItemType.DATE_SEPARATOR

        setVisibility(holder.imageView, employeeAndEmptyVisible)
        setVisibility(holder.textViewName, employeeAndEmptyVisible)
        setVisibility(holder.textViewPosition, employeeAndEmptyVisible)
        setVisibility(holder.textViewUserTag, employeeVisible)
        setVisibility(holder.textViewBirthday, birthdayVisible)
        setVisibility(holder.dividerYear1, yearVisible)
        setVisibility(holder.dividerYear2, yearVisible)
        setVisibility(holder.textViewYear, yearVisible)
    }

    private fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setEmptyStyle(holder: EmployeeViewHolder, empty: Boolean) {
        if (empty) {
            holder.textViewName.text = EMPTY_NAME
            holder.textViewPosition.text = EMPTY_POSITION

            holder.textViewName.setBackgroundResource(R.drawable.textview_backround_empty)
            holder.textViewPosition.setBackgroundResource(R.drawable.textview_backround_empty)
        } else {
            holder.textViewName.setBackgroundResource(R.drawable.textview_backround)
            holder.textViewPosition.setBackgroundResource(R.drawable.textview_backround)
        }
    }

    private fun setEmployeeValues(holder: EmployeeViewHolder, employeeItem: EmployeeItem, setBirthday: Boolean) {
        holder.textViewName.text = employeeItem.employee?.name
        holder.textViewUserTag.text = employeeItem.employee?.userTag?.lowercase()
        holder.textViewPosition.text = employeeItem.employee?.position

        if (setBirthday) {
            holder.textViewBirthday.text =
                employeeItem.employee?.birthdayDate?.format(DateTimeFormatter.ofPattern("dd MMM"))
        }
    }

    private fun setEmployeeEvents(employeeViewHolder: EmployeeViewHolder) {
        employeeViewHolder.itemView.setOnClickListener {
            onClick(employeeViewHolder)
        }

        employeeViewHolder.textViewName.setOnClickListener {
            onClick(employeeViewHolder)
        }

        employeeViewHolder.textViewUserTag.setOnClickListener {
            onClick(employeeViewHolder)
        }

        employeeViewHolder.textViewPosition.setOnClickListener {
            onClick(employeeViewHolder)
        }

        employeeViewHolder.imageView.setOnClickListener {
            onClick(employeeViewHolder)
        }

        employeeViewHolder.textViewBirthday.setOnClickListener {
            onClick(employeeViewHolder)
        }
    }

    private fun onClick(employeeViewHolder: EmployeeViewHolder) {
        val position = employeeViewHolder.absoluteAdapterPosition

        if (position in employeeItems.indices) {
            val employee = employeeItems[position].employee

            if (employee != null) {
                listener.onSelectEmployee(employee)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_employee, parent, false)
        return EmployeeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employeeItem = employeeItems[position]

        setVisibilities(holder, employeeItem)
        setEmptyStyle(holder, employeeItem.itemType == EmployeeItemType.EMPTY)

        when (employeeItem.itemType) {
            EmployeeItemType.EMPTY -> {
                //
            }

            EmployeeItemType.EMPLOYEE -> {
                setEmployeeValues(holder, employeeItem, false)
                setEmployeeEvents(holder)
            }

            EmployeeItemType.EMPLOYEE_DATE -> {
                setEmployeeValues(holder, employeeItem, true)
                setEmployeeEvents(holder)
            }

            EmployeeItemType.DATE_SEPARATOR -> {
                holder.textViewYear.text = employeeItem.date
            }
        }

    }

    override fun getItemCount(): Int {
        return employeeItems.size
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPosition: TextView = itemView.findViewById(R.id.textViewPosition)
        val textViewUserTag: TextView = itemView.findViewById(R.id.textViewUserTag)
        val textViewBirthday: TextView = itemView.findViewById(R.id.textViewBirthday)
        val dividerYear1: View = itemView.findViewById(R.id.dividerYear1)
        val dividerYear2: View = itemView.findViewById(R.id.dividerYear2)
        val textViewYear: TextView = itemView.findViewById(R.id.textViewYear)
    }
}