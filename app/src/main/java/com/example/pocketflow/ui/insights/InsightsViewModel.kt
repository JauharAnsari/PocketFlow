package com.example.pocketflow.ui.insights

import androidx.lifecycle.*
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.data.repository.FinanceRepository
import kotlinx.coroutines.flow.map

class InsightsViewModel(private val repository: FinanceRepository) : ViewModel() {

    val transactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    val highestSpendingCategory: LiveData<String> = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { t -> t.amount } }
            .maxByOrNull { it.value }?.key ?: "No data"
    }

    val categoryBreakdown: LiveData<Map<String, Double>> = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { t -> t.amount } }
    }
}

class InsightsViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsightsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InsightsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
