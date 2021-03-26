package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.SelectedItem
import com.zelyder.stocksapp.presentation.core.toDeltaString
import com.zelyder.stocksapp.presentation.core.toPriceString
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment() {
    private val args: DetailsFragmentArgs by navArgs()

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private lateinit var tvTicker: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvCurrentPrice: TextView
    private lateinit var tvDayDelta: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivFav: ImageView
    private lateinit var btnBuy: MaterialButton
    private lateinit var chart: LineChart
    private lateinit var cgScaleChart: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
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
        viewModel.uploadChart(args.stock.ticker, SelectedItem.MONTH)
        cMonth.isChecked = true
        //TODO: finish web sockets
//        viewModel.subscribeToSocketEvents(args.stock.ticker)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewModel.closeSocket()
    }

    private fun initViews(view: View) {
        tvTicker = view.findViewById(R.id.tvTickerDetails)
        tvCompanyName = view.findViewById(R.id.tvCompanyNameDetails)
        tvCurrentPrice = view.findViewById(R.id.tvCurrentPriceDetails)
        tvDayDelta = view.findViewById(R.id.tvDayDeltaDetails)
        ivBack = view.findViewById(R.id.ivBackDetails)
        ivFav = view.findViewById(R.id.ivFavDetails)
        btnBuy = view.findViewById(R.id.btnBuy)
        chart = view.findViewById(R.id.chart)
        cgScaleChart = view.findViewById(R.id.cgScaleChart)

        var selectedItem: SelectedItem? = null
        var showTime = true
        cgScaleChart.setOnCheckedChangeListener { group, checkedId ->
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
            selectedItem?.let { viewModel.uploadChart(args.stock.ticker, it) }
            chart.marker =
                MyMarkerView(requireContext(), R.layout.chart_marker, args.stock.currency, showTime)


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
            marker = MyMarkerView(requireContext(), R.layout.chart_marker, args.stock.currency, showTime)
        }

        val tempStock = args.stock

        tvTicker.text = tempStock.ticker
        tvCompanyName.text = tempStock.companyName

        tvCurrentPrice.text = toPriceString(tempStock.price, tempStock.currency, resources)
        tvDayDelta.text = toDeltaString(
            tempStock.dayDelta,
            tempStock.dayDeltaPercent,
            tempStock.currency,
            resources
        )

        btnBuy.text = resources.getString(
            R.string.btn_buy_text,
            toPriceString(tempStock.price * COMMISSION_MULTIPLIER, tempStock.currency, resources)
        )

        if (tempStock.dayDelta < 0) {
            tvDayDelta.setTextColor(ContextCompat.getColor(view.context, R.color.red))
        } else {
            tvDayDelta.setTextColor(ContextCompat.getColor(view.context, R.color.green))
        }
        ivFav.visibility = if (tempStock.isFavorite) View.VISIBLE else View.INVISIBLE

        ivBack.setOnClickListener {
            it.findNavController().navigateUp()
        }
    }

}

const val COMMISSION_MULTIPLIER = 1.00083f