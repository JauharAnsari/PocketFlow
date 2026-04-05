package com.example.pocketflow.ui.home

import androidx.lifecycle.*
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.data.repository.FinanceRepository
import kotlinx.coroutines.flow.map

class HomeViewModel(private val repository: FinanceRepository) : ViewModel() {

    val transactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()

    val totalIncome: LiveData<Double> = transactions.map { list ->
        list.filter { it.type == "INCOME" }.sumOf { it.amount }
    }

    val totalExpenses: LiveData<Double> = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    }

    val balance: LiveData<Double> = transactions.map { list ->
        val income = list.filter { it.type == "INCOME" }.sumOf { it.amount }
        val expense = list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        income - expense
    }

    val spendingByCategory: LiveData<Map<String, Double>> = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }
}

class HomeViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
