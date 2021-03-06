package com.zelyder.stocksapp.presentation.stockslist

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.viewModelFactoryProvider

class StocksListFragment : Fragment(), StockListItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var tvStocks: TextView? = null
    private var tvFavorites: TextView? = null


    private val viewModel: StocksListViewModel by viewModels { viewModelFactoryProvider()
        .viewModelFactory()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stocks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvStocks)
        tvStocks = view.findViewById(R.id.tvStocks)
        tvFavorites = view.findViewById(R.id.tvFavourite)

        recyclerView?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = StocksListAdapter(this)

        viewModel.stocksList.observe(this.viewLifecycleOwner, {
            (recyclerView?.adapter as? StocksListAdapter)?.apply {
                bindStocks(it)
            }
        })

        if (savedInstanceState == null) {
            viewModel.updateList()
        }

        viewModel.isFavSelected.observe(this.viewLifecycleOwner, { isFavSelected ->
            isFavSelected?.let { swapTab(it) }
        })

        tvStocks?.setOnClickListener {
            viewModel.swapToStocksTab()
        }

        tvFavorites?.setOnClickListener {
            viewModel.swapToFavTab()
        }

    }

    override fun onDestroyView() {
        recyclerView = null
        tvStocks = null
        tvFavorites = null
        super.onDestroyView()
    }

    override fun onClickFavourite(ticker: String, isFavorite: Boolean) {
        viewModel.updateFavState(ticker, isFavorite)
    }

    private fun swapTab(isFavoriteTab: Boolean) {

        val tvStocksSize: Float
        val tvFavoritesSize: Float

        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)

        if (isFavoriteTab){
            tvStocksSize = resources.getDimension(R.dimen.unselected_tab_size)
            tvFavoritesSize = resources.getDimension(R.dimen.selected_tab_size)
            tvStocks?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            tvFavorites?.setTextColor(typedValue.data)
        }else {
            tvStocksSize = resources.getDimension(R.dimen.selected_tab_size)
            tvFavoritesSize = resources.getDimension(R.dimen.unselected_tab_size)
            tvStocks?.setTextColor(typedValue.data)
            tvFavorites?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        }

        tvStocks?.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvStocksSize)
        tvFavorites?.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvFavoritesSize)
    }
}