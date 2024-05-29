package com.blanks.connect.presenter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blanks.connect.data.Employee
import com.blanks.connect.databinding.ItemDataBinding

class EmployeeAdapter(
    private val employees: ArrayList<Employee>,
    private val listener: Events
) :
    RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    private lateinit var binding: ItemDataBinding

    interface Events {
        fun onDelete(recipe: Employee)
        fun onClickEdit(recipe: Employee)
    }

    inner class ViewHolder(private val binding: ItemDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = employees[position]
        binding.tvName.text = item.staf_name
        binding.tvAddress.text = item.staf_alamat
        binding.tvPhone.text = item.staf_hp

        binding.btnHapus.setOnClickListener {
            listener.onDelete(item)
        }

        binding.card.setOnClickListener {
            listener.onClickEdit(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dispatch(data: ArrayList<Employee>) {
        employees.clear()
        employees.addAll(data)
        notifyDataSetChanged()
    }

}