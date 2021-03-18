package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_stocks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvStocksSearch)
        recyclerView?.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.adapter = StocksListAdapter(this)

        viewModel.stocksList.observe(this.viewLifecycleOwner) {
            (recyclerView?.adapter as? StocksListAdapter)?.apply {
                bindStocks(it)
            }
        }
        arguments?.getString(KEY_QUERY)?.let { viewModel.searchStock(it) }
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

    override fun onClickFavourite(ticker: String, isFavorite: Boolean) {
        viewModel.updateFavState(ticker, isFavorite)
    }
}

const val KEY_QUERY = "keyQueryStocks"