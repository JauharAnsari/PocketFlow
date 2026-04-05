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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        setupSaveButton()
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
                    amount = amount,
                    type = type,
                    category = category,
                    dateTime = System.currentTimeMillis(),
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
