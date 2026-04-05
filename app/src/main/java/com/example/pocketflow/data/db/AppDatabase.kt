package com.example.pocketflow.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pocketflow.data.dao.BudgetDao
import com.example.pocketflow.data.dao.GoalDao
import com.example.pocketflow.data.dao.TransactionDao
import com.example.pocketflow.data.entities.Budget
import com.example.pocketflow.data.entities.Goal
import com.example.pocketflow.data.entities.Transaction

@Database(entities = [Transaction::class, Budget::class, Goal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pocketflow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
