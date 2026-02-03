package com.example.unispend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unispend.R
import com.example.unispend.data.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback) {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)
        val amount: TextView = view.findViewById(R.id.tvAmount)
        val date: TextView = view.findViewById(R.id.tvDate) // Ensure your item_expense.xml has this ID
        val category: TextView = view.findViewById(R.id.tvCategory) // Ensure your item_expense.xml has this ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)

        holder.title.text = item.title

        // FORMAT THE AMOUNT (Green for Income, Red for Expense)
        if (item.type == "INCOME") {
            holder.amount.text = "+ ₦%,.0f".format(item.amount)
            holder.amount.setTextColor(holder.itemView.context.getColor(R.color.accent_green))
        } else {
            holder.amount.text = "- ₦%,.0f".format(item.amount)
            holder.amount.setTextColor(holder.itemView.context.getColor(R.color.error_red))
        }

        // SHOW CATEGORY
        holder.category.text = item.category

        // FIX THE DATE ERROR
        // We convert the 'Long' timestamp into a readable String
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        holder.date.text = sdf.format(Date(item.date))
    }

    // This helps the list update efficiently
    companion object DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}