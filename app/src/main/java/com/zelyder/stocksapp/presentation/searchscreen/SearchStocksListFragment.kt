package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.presentation.stockslist.StockListItemClickListener
import com.zelyder.stocksapp.presentation.stockslist.StocksListAdapter
import com.zelyder.stocksapp.presentation.stockslist.StocksListViewModel
import com.zelyder.stocksapp.viewModelFactoryProvider

class SearchStocksListFragment : Fragment(), StockListItemClickListener {

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private var recyclerView: RecyclerView? = null
    private var pbSearch: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_stocks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvStocksSearch)
        pbSearch = view.findViewById(R.id.pbSearch)

        recyclerView?.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.adapter = StocksListAdapter(this)

        viewModel.stocksList.observe(this.viewLifecycleOwner) {
            (recyclerView?.adapter as? StocksListAdapter)?.apply {
                bindStocks(it)
                pbSearch?.isVisible = false
            }
        }
        arguments?.getString(KEY_QUERY)?.let { viewModel.searchStock(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        pbSearch = null

    }

    companion object {
        fun newInstance(query: String): SearchStocksListFragment {
            val args = Bundle()
            args.putString(KEY_QUERY, query)
            val fragment = SearchStocksListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClickFavourite(stock: Stock) {
        viewModel.updateFavState(stock)
    }
}

const val KEY_QUERY = "keyQueryStocks"