package com.zelyder.stocksapp.presentation.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zelyder.stocksapp.domain.models.Stock
import java.lang.IllegalArgumentException

class ViewPagerAdapter(fragment: Fragment, val stock: Stock) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = CARD_ITEM_SIZE

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ChartFragment.newInstance(stock)
            1 -> SummaryFragment.newInstance(stock.ticker)
            2 -> NewsFragment.newInstance(stock.ticker)
            else -> throw IllegalArgumentException("$position is not registered tab")
        }
    }
}

private const val CARD_ITEM_SIZE = 3