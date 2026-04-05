package com.example.pocketflow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.data.PreferenceManager
import com.example.pocketflow.databinding.FragmentHomeBinding
import com.example.pocketflow.ui.transactions.TransactionAdapter
import java.util.*
import com.github.aachartmodel.aainfographics.aachartcreator.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireActivity().application as PocketFlowApplication).repository)
    }

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        preferenceManager = PreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupRecyclerView()
        updateGreeting()
    }

    override fun onResume() {
        super.onResume()
        updateGreeting()
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val userName = preferenceManager.profileName

        val greeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }

        binding.greetingText.text = "$greeting, $userName 👋"
    }

    private fun setupObservers() {
        homeViewModel.balance.observe(viewLifecycleOwner) { balance ->
            binding.balanceText.text = "₹%.2f".format(balance)
        }

        homeViewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.incomeText.text = "+₹%.2f".format(income)
        }

        homeViewModel.totalExpenses.observe(viewLifecycleOwner) { expense ->
            binding.expenseText.text = "-₹%.2f".format(expense)
        }

        homeViewModel.transactions.observe(viewLifecycleOwner) { list ->
            transactionAdapter.submitList(list.take(5))
        }

        homeViewModel.spendingByCategory.observe(viewLifecycleOwner) { categoryMap ->
            setupSpendingChart(categoryMap)
        }
    }

    private fun setupSpendingChart(categoryMap: Map<String, Double>) {
        if (categoryMap.isEmpty()) {
            binding.spendingChartView.visibility = View.GONE
            return
        }
        binding.spendingChartView.visibility = View.VISIBLE

        val chartData = categoryMap.map { entry ->
            arrayOf<Any>(entry.key, entry.value)
        }.toTypedArray<Any>()

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .backgroundColor("#00000000") // Transparent background
            .dataLabelsEnabled(false)
            .legendEnabled(false)
            .tooltipEnabled(true)
            .colorsTheme(arrayOf("#8B5CF6", "#EC4899", "#F59E0B", "#3B82F6", "#10B981"))
            .series(arrayOf(
                AASeriesElement()
                    .name("Spending")
                    .data(chartData)
                    .innerSize("75%") // Creates the donut effect
            ))

        binding.spendingChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter()
        binding.recentTransactionsList.layoutManager = LinearLayoutManager(context)
        binding.recentTransactionsList.adapter = transactionAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
