package com.example.pocketflow.ui.goals

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketflow.data.entities.Budget
import com.example.pocketflow.databinding.ItemBudgetBinding

class BudgetAdapter : ListAdapter<Pair<Budget, Double>, BudgetAdapter.BudgetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BudgetViewHolder(private val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<Budget, Double>) {
            val budget = pair.first
            val spent = pair.second
            val progress = ((spent / budget.limitAmount) * 100).toInt().coerceIn(0, 100)

            binding.categoryText.text = budget.category
            binding.spentText.text = "₹%.2f spent".format(spent)
            binding.limitText.text = "of ₹%.2f".format(budget.limitAmount)
            binding.amountRemainingText.text = "₹%.2f left".format((budget.limitAmount - spent).coerceAtLeast(0.0))
            
            binding.budgetProgressBar.progress = progress

            // Color logic: Green -> Yellow (80%) -> Red
            val color = when {
                progress >= 100 -> Color.RED
                progress >= 80 -> Color.YELLOW
                else -> Color.GREEN
            }
            // In a real app, use theme colors. For now, simple colors.
            binding.budgetProgressBar.setIndicatorColor(color)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pair<Budget, Double>>() {
        override fun areItemsTheSame(oldItem: Pair<Budget, Double>, newItem: Pair<Budget, Double>) = 
            oldItem.first.id == newItem.first.id
        override fun areContentsTheSame(oldItem: Pair<Budget, Double>, newItem: Pair<Budget, Double>) = 
            oldItem == newItem
    }
}
