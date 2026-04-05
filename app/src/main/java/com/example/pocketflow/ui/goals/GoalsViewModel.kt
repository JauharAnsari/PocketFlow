package com.example.pocketflow.ui.goals

import android.app.Application
import androidx.lifecycle.*
import com.example.pocketflow.data.entities.Budget
import com.example.pocketflow.data.entities.Goal
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.data.repository.FinanceRepository
import com.example.pocketflow.ui.notifications.NotificationHelper
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GoalsViewModel(application: Application, private val repository: FinanceRepository) : AndroidViewModel(application) {

    private val currentMonthYear = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())

    val transactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()
    val budgets: LiveData<List<Budget>> = repository.getBudgetsByMonth(currentMonthYear).asLiveData()
    val goal: LiveData<List<Goal>> = repository.getGoalByMonth(currentMonthYear).asLiveData()

    val monthlySavings: LiveData<Double> = transactions.map { list ->
        val income = list.filter { it.type == "INCOME" }.sumOf { it.amount }
        val expense = list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
        income - expense
    }

    val savingsProgress: LiveData<Int> = combine(monthlySavings.asFlow(), goal.asFlow()) { savings, goals ->
        val target = goals.firstOrNull()?.targetAmount ?: 1.0
        ((savings / target) * 100).toInt().coerceIn(0, 100)
    }.asLiveData()

    fun addBudget(category: String, limit: Double) {
        val newBudget = Budget(
            category = category,
            limitAmount = limit,
            monthYear = currentMonthYear
        )
        viewModelScope.launch {
            repository.insertBudget(newBudget)
            
            // Check if current spending already exceeds the new limit
            checkBudgetExceedsOnAdd(category, limit)
        }
    }

    private suspend fun checkBudgetExceedsOnAdd(category: String, limit: Double) {
        val transactionsList = repository.allTransactions.firstOrNull() ?: return
        val currentSpending = transactionsList.filter { 
            it.type == "EXPENSE" && 
            it.category == category &&
            isSameMonth(it.dateTime, currentMonthYear)
        }.sumOf { it.amount }

        if (currentSpending >= limit) {
            NotificationHelper.showBudgetAlert(getApplication(), category, limit)
        }
    }

    private fun isSameMonth(timestamp: Long, monthYear: String): Boolean {
        val df = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return df.format(Date(timestamp)) == monthYear
    }
}

class GoalsViewModelFactory(private val application: Application, private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalsViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
