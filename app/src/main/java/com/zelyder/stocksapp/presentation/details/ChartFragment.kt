package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.enums.SelectedItem
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.presentation.core.toDeltaString
import com.zelyder.stocksapp.presentation.core.toPriceString
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.android.synthetic.main.fragment_chart.*


class ChartFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private lateinit var tvCurrentPrice: TextView
    private lateinit var tvDayDelta: TextView
    private lateinit var btnBuy: MaterialButton
    private lateinit var chart: LineChart
    private lateinit var cgScaleChart: ChipGroup

    private var stock: Stock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { stock = it.getParcelable(ARG_STOCK) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)

        viewModel.stockCandle.observe(this.viewLifecycleOwner) {
            val entries: MutableList<Entry> = mutableListOf()
            for (i in it.closePrices.indices) {
                entries.add(Entry(it.timeStamps[i].toFloat(), it.closePrices[i]))
            }
            val dataSet = LineDataSet(entries, "Label")
            dataSet.apply {
                color = typedValue.data
                valueTextColor = typedValue.data
                setDrawCircles(false)
                setDrawValues(false)
                setDrawFilled(true)
                fillDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.chart_fill_gradient)
                setDrawHorizontalHighlightIndicator(false)
                highLightColor = typedValue.data
                highlightLineWidth = 1.0f
            }


            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()

        }
        stock?.ticker?.let { viewModel.uploadChart(it, SelectedItem.MONTH) }
        cMonth.isChecked = true
        // There should have been a web socket subscription, but something went wrong :(
//        viewModel.subscribeToSocketEvents(stock.ticker)
    }


    private fun initViews(view: View) {
        tvCurrentPrice = view.findViewById(R.id.tvCurrentPriceDetails)
        tvDayDelta = view.findViewById(R.id.tvDayDeltaDetails)
        btnBuy = view.findViewById(R.id.btnBuy)
        chart = view.findViewById(R.id.chart)
        cgScaleChart = view.findViewById(R.id.cgScaleChart)

        var selectedItem: SelectedItem? = null
        var showTime = true
        cgScaleChart.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.cDay -> {
                    selectedItem = SelectedItem.DAY
                    showTime = true
                }
                R.id.cWeek -> {
                    selectedItem = SelectedItem.WEEK
                    showTime = true
                }
                R.id.cMonth -> {
                    selectedItem = SelectedItem.MONTH
                    showTime = false
                }
                R.id.cSixMonths -> {
                    selectedItem = SelectedItem.SIX_MONTHS
                    showTime = false
                }
                R.id.cYear -> {
                    selectedItem = SelectedItem.YEAR
                    showTime = false
                }
            }
            selectedItem?.let { sItem -> stock?.ticker?.let { tempStock -> viewModel.uploadChart(tempStock, sItem) } }
            chart.marker =
                stock?.let { MyMarkerView(requireContext(), R.layout.chart_marker, it.currency, showTime) }


        }

        chart.apply {
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            xAxis.isEnabled = false
            axisLeft.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
            description = Description().also { it.text = "" }
            isAutoScaleMinMaxEnabled = true
            marker = MyMarkerView(requireContext(), R.layout.chart_marker, stock?.currency ?: "", showTime)
        }


        stock?.let {
            tvCurrentPrice.text = toPriceString(it.price, it.currency, resources)
            tvDayDelta.text = toDeltaString(
                it.dayDelta,
                it.dayDeltaPercent,
                it.currency,
                resources
            )
        }


        stock?.let {
            // Share price including commission
            btnBuy.text = resources.getString(
                R.string.btn_buy_text,
                toPriceString(it.price * COMMISSION_MULTIPLIER, it.currency, resources)
            )

            if (it.dayDelta < 0) {
                tvDayDelta.setTextColor(ContextCompat.getColor(view.context, R.color.red))
            } else {
                tvDayDelta.setTextColor(ContextCompat.getColor(view.context, R.color.green))
            }
        }
    }

    companion object {
        @JvmStatic
        fun  newInstance(stock: Stock) = ChartFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_STOCK, stock)
            }
        }
    }

}

const val COMMISSION_MULTIPLIER = 1.00083f
const val ARG_STOCK = "arg_stock"