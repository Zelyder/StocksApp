package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.presentation.stockslist.StockListItemClickListener
import com.zelyder.stocksapp.presentation.stockslist.StocksListAdapter
import com.zelyder.stocksapp.presentation.stockslist.StocksLoadStateAdapter
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collect

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
    private val stocksAdapter = StocksListAdapter(this)

    private var query: String? = null
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_stocks_list, container, false)
    }

    @InternalCoroutinesApi
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
        initAdapter()
        query = arguments?.getString(KEY_QUERY)

        query?.let {
            initSearch(it)
            search(it)
        }
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

    @InternalCoroutinesApi
    private fun initSearch(query: String) {
        lifecycleScope.launch {
            stocksAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading && stocksAdapter.itemCount != 0}
                .collect { viewModel.saveQuery(query) }
        }
    }

    @FlowPreview
    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchStock(query).collectLatest {
                stocksAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        recyclerView?.adapter = stocksAdapter
            .withLoadStateFooter(StocksLoadStateAdapter { stocksAdapter.retry() })
        stocksAdapter.addLoadStateListener { loadState ->

            // show empty list
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && stocksAdapter.itemCount == 0
            showEmptyList(isListEmpty)

            recyclerView?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            pbSearch?.isVisible = loadState.source.refresh is LoadState.Loading

            // Show the retry state if initial load or refresh fails.
            if (loadState.source.refresh is LoadState.Error) {
                searchJob?.cancel()
                tvErrorText?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                ivNoConnection?.visibility = View.VISIBLE
                tvErrorText?.setText(R.string.tv_no_connection_error_text)
            }

            // Toast on any error
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            tvErrorText?.text = getString(R.string.no_results)
            tvErrorText?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            tvErrorText?.text = getString(R.string.tv_no_connection_error_text)
            tvErrorText?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }
}

const val KEY_QUERY = "keyQueryStocks"