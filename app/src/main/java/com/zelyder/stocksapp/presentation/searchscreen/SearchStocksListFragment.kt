package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.presentation.stockslist.StockListItemClickListener
import com.zelyder.stocksapp.presentation.stockslist.StocksListAdapter
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
class SearchStocksListFragment : Fragment(), StockListItemClickListener {

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private var recyclerView: RecyclerView? = null
    private var pbSearch: ProgressBar? = null
    private var tvErrorText: TextView? = null
    private var ivNoConnection: ImageView? = null
    private  lateinit var stocksAdapter: StocksListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_stocks_list, container, false)
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvStocksSearch)
        pbSearch = view.findViewById(R.id.pbSearch)
        tvErrorText = view.findViewById(R.id.tvErrorTextSearch)
        ivNoConnection = view.findViewById(R.id.ivNoConnection)

        recyclerView?.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        stocksAdapter = StocksListAdapter(this)
        recyclerView?.adapter = stocksAdapter

        viewModel.stocksList.observe(this.viewLifecycleOwner,  ::handleSearchListResult)
        viewModel.searchState.observe(viewLifecycleOwner, ::handleLoadingState)
        arguments?.getString(KEY_QUERY)?.let { viewModel.searchStock(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        pbSearch = null

    }

    private fun handleLoadingState(state: SearchState) {
        when (state) {
            Loading -> {
                pbSearch?.visibility = View.VISIBLE
            }
            Ready -> {
                pbSearch?.visibility = View.GONE
            }
        }
    }

    private fun handleSearchListResult(result: SearchResult) {
        when (result) {
            is ValidResult -> {
                    stocksAdapter.bindStocks(result.result)
            }
            is ErrorResult -> {
                stocksAdapter.bindStocks(emptyList())
                tvErrorText?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                ivNoConnection?.visibility = View.VISIBLE
                tvErrorText?.setText(R.string.tv_searched_error_text)
                Log.e(this::class.java.name, "Something went wrong.", result.e)
            }
            is EmptyResult -> {
                stocksAdapter.bindStocks(emptyList())
                tvErrorText?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                ivNoConnection?.visibility = View.GONE
                tvErrorText?.setText(R.string.tv_searched_empty_result_text)
            }
            is EmptyQuery -> {
                stocksAdapter.bindStocks(emptyList())
                tvErrorText?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                ivNoConnection?.visibility = View.GONE
                tvErrorText?.setText(R.string.tv_searched_placeholder_text)
            }
            is TerminalError -> {
                Toast.makeText(
                    activity,
                    getString(R.string.tv_searched_error_unknown_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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