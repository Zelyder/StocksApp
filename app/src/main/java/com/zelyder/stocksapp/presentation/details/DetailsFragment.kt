package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zelyder.stocksapp.R
import java.lang.IllegalArgumentException


class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var ivBack: ImageView

    private lateinit var tvTicker: TextView
    private lateinit var tvCompanyName: TextView
    private lateinit var ivFav: ImageView

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
        viewPager = view.findViewById(R.id.vpDetails)
        tabLayout = view.findViewById(R.id.tabLayoutDetails)

        tvTicker = view.findViewById(R.id.tvTickerDetails)
        tvCompanyName = view.findViewById(R.id.tvCompanyNameDetails)
        ivFav = view.findViewById(R.id.ivFavDetails)
        ivBack = view.findViewById(R.id.ivBackDetails)

        val tempStock = args.stock

        tvTicker.text = tempStock.ticker
        tvCompanyName.text = tempStock.companyName
        ivFav.visibility = if (tempStock.isFavorite) View.VISIBLE else View.INVISIBLE

        viewPager.adapter = ViewPagerAdapter(this, args.stock)
        TabLayoutMediator(tabLayout, viewPager, true) { tab, position ->
            tab.setText(
                when(position) {
                    0 -> R.string.tab_chart
                    1 -> R.string.tab_summary
                    2 -> R.string.tab_news
                    else -> throw IllegalArgumentException("$position is not registered tab")
                }
            )
            viewPager.setCurrentItem(tab.position, true)
        }.attach()

        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}