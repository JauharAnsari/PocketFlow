package com.example.pocketflow.ui.goals

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.databinding.DialogAddBudgetBinding
import com.example.pocketflow.databinding.FragmentGoalsBinding
import com.example.pocketflow.ui.notifications.NotificationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    private val goalsViewModel: GoalsViewModel by viewModels {
        val application = requireActivity().application as PocketFlowApplication
        GoalsViewModelFactory(application, application.repository)
    }

    private lateinit var budgetAdapter: BudgetAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            NotificationHelper.createNotificationChannel(requireContext())
        } else {
            Toast.makeText(requireContext(), "Notifications disabled. You won't get budget alerts.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupListeners()
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationHelper.createNotificationChannel(requireContext())
            }
        } else {
            NotificationHelper.createNotificationChannel(requireContext())
        }
    }

    private fun setupListeners() {
        binding.addBudgetBtn.setOnClickListener {
            showAddBudgetDialog()
        }
    }

    private fun showAddBudgetDialog() {
        val dialogBinding = DialogAddBudgetBinding.inflate(layoutInflater)
        
        val categories = arrayOf("Food", "Bills", "Shopping", "Transport", "Entertainment", "Health", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        (dialogBinding.categoryInput as? AutoCompleteTextView)?.setAdapter(adapter)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Category Goal")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val category = dialogBinding.categoryInput.text.toString()
                val limit = dialogBinding.limitInput.text.toString().toDoubleOrNull() ?: 0.0
                
                if (category.isNotEmpty() && limit > 0) {
                    goalsViewModel.addBudget(category, limit)
                } else {
                    Toast.makeText(requireContext(), "Please enter valid details", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter()
        binding.budgetList.layoutManager = LinearLayoutManager(context)
        binding.budgetList.adapter = budgetAdapter
    }

    private fun setupObservers() {
        goalsViewModel.savingsProgress.observe(viewLifecycleOwner) { progress ->
            binding.savingsProgressBar.progress = progress
            binding.savingsPercentText.text = "$progress%"
            
            if (progress >= 100) {
                binding.goalStatusText.text = "Goal Achieved! 🎉"
            }
        }

        goalsViewModel.budgets.observe(viewLifecycleOwner) { budgetList ->
            goalsViewModel.transactions.observe(viewLifecycleOwner) { transactionList ->
                val pairs = budgetList.map { budget ->
                    val spent = transactionList
                        .filter { it.type == "EXPENSE" && it.category == budget.category }
                        .sumOf { it.amount }
                    Pair(budget, spent)
                }
                budgetAdapter.submitList(pairs)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
