package com.example.pocketflow

import android.app.Application
import com.example.pocketflow.data.db.AppDatabase
import com.example.pocketflow.data.repository.FinanceRepository

class PocketFlowApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { 
        FinanceRepository(
            database.transactionDao(),
            database.budgetDao(),
            database.goalDao()
        ) 
    }
}
