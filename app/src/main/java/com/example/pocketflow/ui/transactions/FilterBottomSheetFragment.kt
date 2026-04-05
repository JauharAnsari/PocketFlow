package com.example.pocketflow.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.R
import com.example.pocketflow.databinding.LayoutFilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: LayoutFilterBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: TransactionViewModel by activityViewModels {
        val application = requireActivity().application as PocketFlowApplication
        TransactionViewModelFactory(application, application.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val currentFilter = transactionViewModel.filterState.value ?: return

        // Set Type
        when (currentFilter.type) {
            "INCOME" -> binding.chipIncome.isChecked = true
            "EXPENSE" -> binding.chipExpense.isChecked = true
            else -> binding.chipAllTypes.isChecked = true
        }

        // Set Date
        when (currentFilter.dateRange) {
            "TODAY" -> binding.chipToday.isChecked = true
            "WEEK" -> binding.chipThisWeek.isChecked = true
            "MONTH" -> binding.chipThisMonth.isChecked = true
            else -> binding.chipAllDate.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.applyButton.setOnClickListener {
            val type = when (binding.typeChipGroup.checkedChipId) {
                R.id.chipIncome -> "INCOME"
                R.id.chipExpense -> "EXPENSE"
                else -> null
            }

            val dateRange = when (binding.dateChipGroup.checkedChipId) {
                R.id.chipToday -> "TODAY"
                R.id.chipThisWeek -> "WEEK"
                R.id.chipThisMonth -> "MONTH"
                else -> "ALL"
            }

            transactionViewModel.updateFilter(type, dateRange)
            dismiss()
        }

        binding.clearButton.setOnClickListener {
            binding.chipAllTypes.isChecked = true
            binding.chipAllDate.isChecked = true
            transactionViewModel.updateFilter(null, "ALL")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FilterBottomSheetFragment"
    }
}
