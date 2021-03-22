package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.button.MaterialButton
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.presentation.core.toDeltaString
import com.zelyder.stocksapp.presentation.core.toPriceString


class DetailsFragment : Fragment() {
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var tvTicker: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var tvCurrentPrice: TextView
    private lateinit var tvDayDelta: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivFav: ImageView
    private lateinit var btnBuy: MaterialButton
    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
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