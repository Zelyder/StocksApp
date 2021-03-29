package com.zelyder.stocksapp.presentation.stockslist

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StocksListFragment : Fragment(), StockListItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var tvStocks: TextView? = null
    private var tvFavorites: TextView? = null
    private var searchView: SearchView? = null
    private var tvErrorText: TextView? = null
    private var ivNoConnection: ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var btnRetry: Button? = null
    private var progressBar: ProgressBar? = null

    private var isFavoriteTab: Boolean = false
    private var updateJob: Job? = null
    private val adapter = StocksListAdapter(this)


    private val viewModel: StocksListViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }


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
        searchView = view.findViewById(R.id.start_searchView)
        tvErrorText = view.findViewById(R.id.tvErrorTextMainList)
        ivNoConnection = view.findViewById(R.id.ivNoConnectionMainList)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutMain)
        btnRetry = view.findViewById(R.id.btnRetryMainList)
        progressBar = view.findViewById(R.id.pbMainList)

        recyclerView?.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        initAdapter()



        swipeRefreshLayout?.setOnRefreshListener {
            if (isFavoriteTab) {
                swapToFavTab(true)
            } else {
                updateList(true)
            }
        }
        swipeRefreshLayout?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        if (savedInstanceState == null) {
            initList()
            updateList()
        }

        viewModel.isFavSelected.observe(this.viewLifecycleOwner) { isFavSelected ->
            isFavSelected?.let {
                swapTab(it)
                isFavoriteTab = it
            }
        }

        btnRetry?.setOnClickListener {
            adapter.retry()
        }

        tvStocks?.setOnClickListener {
            swapToStocksTab()
        }

        tvFavorites?.setOnClickListener {
            swapToFavTab()
        }

        searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                findNavController().navigate(StocksListFragmentDirections.actionStocksListFragmentToSearchFragment())
            }
        }


    }

    override fun onDestroyView() {
        recyclerView = null
        tvStocks = null
        tvFavorites = null
        searchView = null
        tvErrorText = null
        ivNoConnection = null
        swipeRefreshLayout = null
        btnRetry = null
        progressBar = null
        viewModel.resetTabState()
        super.onDestroyView()
    }

    override fun onClickFavourite(stock: Stock) {
        viewModel.updateFavState(stock)
    }

    private fun swapTab(isFavoriteTab: Boolean) {

        val tvStocksSize: Float
        val tvFavoritesSize: Float

        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true)

        if (isFavoriteTab) {
            tvStocksSize = resources.getDimension(R.dimen.unselected_tab_size)
            tvFavoritesSize = resources.getDimension(R.dimen.selected_tab_size)
            tvStocks?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            tvFavorites?.setTextColor(typedValue.data)
        } else {
            tvStocksSize = resources.getDimension(R.dimen.selected_tab_size)
            tvFavoritesSize = resources.getDimension(R.dimen.unselected_tab_size)
            tvStocks?.setTextColor(typedValue.data)
            tvFavorites?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
        }

        tvStocks?.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvStocksSize)
        tvFavorites?.setTextSize(TypedValue.COMPLEX_UNIT_PX, tvFavoritesSize)
    }

    private fun showNoConnectionText() {
        tvErrorText?.visibility = View.VISIBLE
        ivNoConnection?.visibility = View.VISIBLE
    }

    private fun hideNoConnectionText() {
        tvErrorText?.visibility = View.GONE
        ivNoConnection?.visibility = View.GONE
    }

    private fun updateList(forceRefresh: Boolean = false) {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch {
            viewModel.updatedList(forceRefresh).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun swapToFavTab(forceRefresh: Boolean = false) {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch {
            viewModel.swapToFavTab(forceRefresh)?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun swapToStocksTab() {
        updateJob?.cancel()
        updateJob = lifecycleScope.launch {
            viewModel.swapToStocksTab()?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initList() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    recyclerView?.scrollToPosition(0)
                    swipeRefreshLayout?.isRefreshing = false
                }
        }
    }

    private fun initAdapter() {
        recyclerView?.adapter =
            adapter.withLoadStateFooter(StocksLoadStateAdapter { adapter.retry() })
        adapter.addLoadStateListener { loadState ->
            // show empty list
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            // Only show the list if refresh succeeds.
            recyclerView?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.

            if (loadState.source.refresh is LoadState.Error) {
                swipeRefreshLayout?.isRefreshing = false
                btnRetry?.isVisible = true
                showNoConnectionText()
            } else {
                btnRetry?.isVisible = false
                hideNoConnectionText()
            }

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
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
            swipeRefreshLayout?.isRefreshing = false
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