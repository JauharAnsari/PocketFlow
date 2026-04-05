package com.example.pocketflow.ui.transactions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.R
import com.example.pocketflow.databinding.FragmentTransactionListBinding
import com.example.pocketflow.data.entities.Transaction

class TransactionListFragment : Fragment() {

    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(requireActivity().application, (requireActivity().application as PocketFlowApplication).repository)
    }

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupSearch()
        setupFilterButton()
        setupSwipeToDelete()
    }

    private fun setupFilterButton() {
        binding.filterButton.setOnClickListener {
            FilterBottomSheetFragment().show(childFragmentManager, FilterBottomSheetFragment.TAG)
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter()
        binding.transactionList.layoutManager = LinearLayoutManager(context)
        binding.transactionList.adapter = transactionAdapter
    }

    private fun setupObservers() {
        transactionViewModel.filteredTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    private fun setupSearch() {
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                transactionViewModel.updateSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    // Delete the transaction
                    transactionViewModel.delete(transaction)

                    // Show Snackbar with Undo option
                    Snackbar.make(binding.root, "Transaction deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            transactionViewModel.insert(transaction)
                        }.show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Edit the transaction
                    val bundle = Bundle().apply {
                        putLong("transactionId", transaction.id)
                    }
                    findNavController().navigate(R.id.navigation_add_transaction, bundle)
                    
                    // Reset the item position so it stays in the list
                    transactionAdapter.notifyItemChanged(position)
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.transactionList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
