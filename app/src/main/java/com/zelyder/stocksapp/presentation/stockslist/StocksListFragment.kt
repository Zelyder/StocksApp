package com.zelyder.stocksapp.presentation.stockslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.data.DataSource
import com.zelyder.stocksapp.viewModelFactoryProvider

class StocksListFragment : Fragment(), StockListItemClickListener {

    private var recyclerView: RecyclerView? = null


    private val viewModel: StocksListViewModel by lazy { viewModelFactoryProvider()
        .viewModelFactory().create(StocksListViewModel::class.java)}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stocks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvStocks)
        recyclerView?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = StocksListAdapter(this)

        viewModel.stocksList.observe(this.viewLifecycleOwner, {
            (recyclerView?.adapter as? StocksListAdapter)?.apply {
                bindStocks(it)
            }
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.updateList()
    }

    override fun onDestroyView() {
        recyclerView = null
        super.onDestroyView()
    }

    override fun onClickFavourite(ticker: String, isFavorite: Boolean) {
        viewModel.updateFavState(ticker, isFavorite)
    }
}