package com.example.unispend.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unispend.R
import com.example.unispend.data.Goal

class GoalAdapter(private val onItemClicked: (Goal) -> Unit) : ListAdapter<Goal, GoalAdapter.GoalViewHolder>(DiffCallback) {

    class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvGoalTitle)
        val progressText: TextView = view.findViewById(R.id.tvGoalProgress)
        val amountText: TextView = view.findViewById(R.id.tvGoalAmount)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title

        val percent = if (item.targetAmount > 0) (item.savedAmount / item.targetAmount) * 100 else 0.0
        holder.progressBar.progress = percent.toInt()
        holder.progressText.text = "${percent.toInt()}%"
        holder.amountText.text = "₦${"%,.0f".format(item.savedAmount)} / ₦${"%,.0f".format(item.targetAmount)}"

        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Goal, newItem: Goal) = oldItem == newItem
    }
}