package com.zelyder.stocksapp.presentation.details


import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.presentation.core.toDate
import com.zelyder.stocksapp.presentation.core.toPriceString


@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context, layoutResource: Int, val currency: String,val showTime: Boolean = false) : MarkerView(context, layoutResource) {
    private var tvPrice: TextView? = null
    private var tvDate: TextView? = null

    init {
        tvPrice = findViewById(R.id.tvCurrentPriceMarker)
        tvDate = findViewById(R.id.tvDateMarker)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            tvDate?.text = (e.x*1000).toLong().toDate(showTime)
            tvPrice?.text = toPriceString(e.y, currency, resources)
        }

        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }
}