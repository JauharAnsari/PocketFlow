package com.example.pocketflow.ui.transactions

import android.app.Application
import androidx.lifecycle.*
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.data.repository.FinanceRepository
import com.example.pocketflow.ui.notifications.NotificationHelper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


data class TransactionFilter(
    val type: String? = null, // "INCOME", "EXPENSE", or null for All
    val dateRange: String = "ALL", // "TODAY", "WEEK", "MONTH", "ALL"
    val searchQuery: String = ""
)

class TransactionViewModel(application: Application, private val repository: FinanceRepository) : AndroidViewModel(application) {

    private val _filterState = MutableLiveData(TransactionFilter())
    val filterState: LiveData<TransactionFilter> = _filterState

    val filteredTransactions: LiveData<List<Transaction>> = MediatorLiveData<List<Transaction>>().apply {
        var transactions: List<Transaction> = emptyList()
        var filter = TransactionFilter()

        fun update() {
            value = transactions.filter { t ->
                // Apply Search
                val matchesSearch = filter.searchQuery.isEmpty() || 
                    t.category.contains(filter.searchQuery, ignoreCase = true) || 
                    (t.notes?.contains(filter.searchQuery, ignoreCase = true) ?: false)
                
                // Apply Type
                val matchesType = filter.type == null || t.type == filter.type
                
                // Apply Date
                val matchesDate = when (filter.dateRange) {
                    "TODAY" -> isSameDay(t.dateTime, System.currentTimeMillis())
                    "WEEK" -> isWithinThisWeek(t.dateTime)
                    "MONTH" -> isSameMonth(t.dateTime, SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date()))
                    else -> true
                }

                matchesSearch && matchesType && matchesDate
            }
        }

        addSource(repository.allTransactions.asLiveData()) { 
            transactions = it
            update()
        }
        addSource(_filterState) { 
            filter = it
            update()
        }
    }

    fun updateFilter(type: String?, dateRange: String) {
        _filterState.value = _filterState.value?.copy(type = type, dateRange = dateRange)
    }

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value?.copy(searchQuery = query)
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val df = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return df.format(Date(t1)) == df.format(Date(t2))
    }

    private fun isWithinThisWeek(timestamp: Long): Boolean {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return timestamp >= cal.timeInMillis
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insertTransaction(transaction)
        
        if (transaction.type == "EXPENSE") {
            checkBudgetExceeded(transaction)
        }
    }

    private suspend fun checkBudgetExceeded(transaction: Transaction) {
        val currentMonthYear = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())
        
        // 1. Get budgets for this month
        val budgets = repository.getBudgetsByMonth(currentMonthYear).firstOrNull()
        val categoryBudget = budgets?.find { it.category == transaction.category } ?: return

        // 2. Get all transactions for this month to calculate current spending
        val transactions = repository.allTransactions.firstOrNull() ?: return
        val currentSpending = transactions.filter { 
            it.type == "EXPENSE" && 
            it.category == transaction.category &&
            isSameMonth(it.dateTime, currentMonthYear)
        }.sumOf { it.amount }

        // 3. If limit exceeded, notify
        if (currentSpending >= categoryBudget.limitAmount) {
            NotificationHelper.showBudgetAlert(getApplication(), transaction.category, categoryBudget.limitAmount)
        }
    }

    private fun isSameMonth(timestamp: Long, monthYear: String): Boolean {
        val df = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        return df.format(Date(timestamp)) == monthYear
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }
}

class TransactionViewModelFactory(private val application: Application, private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
