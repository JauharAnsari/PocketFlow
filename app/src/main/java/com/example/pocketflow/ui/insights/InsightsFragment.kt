package com.example.pocketflow.ui.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pocketflow.PocketFlowApplication
import com.example.pocketflow.databinding.FragmentInsightsBinding
import com.github.aachartmodel.aainfographics.aachartcreator.*

class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null
    private val binding get() = _binding!!

    private val insightsViewModel: InsightsViewModel by viewModels {
        InsightsViewModelFactory((requireActivity().application as PocketFlowApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        insightsViewModel.highestSpendingCategory.observe(viewLifecycleOwner) { category ->
            binding.highestSpendingCategoryText.text = category
        }

        insightsViewModel.categoryBreakdown.observe(viewLifecycleOwner) { breakdown ->
            setupPieChart(breakdown)
        }
        
        // Weekly trend would involve more complex data preparation
        setupWeeklyTrendChart()
    }

    private fun setupPieChart(breakdown: Map<String, Double>) {
        val data = breakdown.map { arrayOf<Any>(it.key, it.value) }.toTypedArray()
        
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Category Breakdown")
            .subtitle("Amount spent this month")
            .backgroundColor("#00000000") // Transparent
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Amount")
                    .data(data as Array<Any>)
            ))

        binding.categoryPieChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun setupWeeklyTrendChart() {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("Weekly Spending")
            .backgroundColor("#00000000")
            .series(arrayOf(
                AASeriesElement()
                    .name("This Week")
                    .data(arrayOf<Any>(2000, 4500, 3000, 5000, 2000, 6000, 4000)),
                AASeriesElement()
                    .name("Last Week")
                    .data(arrayOf<Any>(3000, 2000, 4000, 3500, 5000, 2000, 3000))
            ))

        binding.weeklyTrendChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
