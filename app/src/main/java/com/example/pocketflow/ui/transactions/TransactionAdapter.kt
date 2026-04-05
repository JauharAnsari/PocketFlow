package com.example.pocketflow.ui.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.databinding.ItemTransactionRecentBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionRecentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

        fun bind(transaction: Transaction) {
            binding.categoryText.text = transaction.category
            binding.dateText.text = dateFormat.format(Date(transaction.dateTime))
            
            val sign = if (transaction.type == "INCOME") "+" else "-"
            binding.amountText.text = "%s₹%.2f".format(sign, transaction.amount)
            
            val colorRes = if (transaction.type == "INCOME") {
                android.R.color.holo_green_dark
            } else {
                android.R.color.holo_red_dark
            }
            binding.amountText.setTextColor(binding.root.context.getColor(colorRes))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem == newItem
    }
}
