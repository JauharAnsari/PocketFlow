package com.example.pocketflow.data.repository

import com.example.pocketflow.data.dao.BudgetDao
import com.example.pocketflow.data.dao.GoalDao
import com.example.pocketflow.data.dao.TransactionDao
import com.example.pocketflow.data.entities.Budget
import com.example.pocketflow.data.entities.Goal
import com.example.pocketflow.data.entities.Transaction
import kotlinx.coroutines.flow.Flow

class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val goalDao: GoalDao
) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    fun getTransactionsByDateRange(start: Long, end: Long) = 
        transactionDao.getTransactionsByDateRange(start, end)

    fun getBudgetsByMonth(monthYear: String) = budgetDao.getBudgetsByMonth(monthYear)

    fun getGoalByMonth(monthYear: String) = goalDao.getGoalByMonth(monthYear)

    suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
    }
}
