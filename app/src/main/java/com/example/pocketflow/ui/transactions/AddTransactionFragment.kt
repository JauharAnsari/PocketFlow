package com.example.pocketflow.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.data.entities.Transaction
import com.example.pocketflow.databinding.FragmentAddTransactionBinding

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(requireActivity().application, (requireActivity().application as PocketFlowApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var editTransactionId: Long = -1L
    private var originalTimestamp: Long = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        
        // Check for edit mode
        editTransactionId = arguments?.getLong("transactionId", -1L) ?: -1L
        if (editTransactionId != -1L) {
            setupEditMode()
        }
        
        setupSaveButton()
    }

    private fun setupEditMode() {
        val transaction = transactionViewModel.getTransactionById(editTransactionId)
        transaction?.let {
            originalTimestamp = it.dateTime
            binding.amountInput.setText(it.amount.toString())
            
            if (it.type == "INCOME") {
                binding.incomeButton.isChecked = true
            } else {
                binding.expenseButton.isChecked = true
            }
            
            val categories = arrayOf("Food", "Transport", "Shopping", "Bills", "Health", "Entertainment", "Other")
            val index = categories.indexOf(it.category)
            if (index != -1) {
                binding.categorySpinner.setSelection(index)
            }
            
            binding.saveButton.text = "Update Transaction"
            // Change title text if it exists in layout, or just button
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Food", "Transport", "Shopping", "Bills", "Health", "Entertainment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val amountStr = binding.amountInput.text.toString()
            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toDouble()
                val type = if (binding.incomeButton.isChecked) "INCOME" else "EXPENSE"
                val category = binding.categorySpinner.selectedItem.toString()
                
                val transaction = Transaction(
                    id = if (editTransactionId != -1L) editTransactionId else 0,
                    amount = amount,
                    type = type,
                    category = category,
                    dateTime = if (originalTimestamp != -1L) originalTimestamp else System.currentTimeMillis(),
                    currencyCode = "INR"
                )
                
                transactionViewModel.insert(transaction)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
