package com.zelyder.stocksapp.presentation.stockslist

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.viewModelFactoryProvider

class StocksListFragment : Fragment(), StockListItemClickListener {

    private var recyclerView: RecyclerView? = null
    private var tvStocks: TextView? = null
    private var tvFavorites: TextView? = null
    private var searchView: SearchView? = null
    private var tvErrorText: TextView? = null
    private var ivNoConnection: ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var isFavoriteTab: Boolean = false


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

        recyclerView?.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView?.adapter = StocksListAdapter(this)

        viewModel.stocksList.observe(this.viewLifecycleOwner) {
            (recyclerView?.adapter as? StocksListAdapter)?.apply {
                bindStocks(it)
                swipeRefreshLayout?.isRefreshing = false
                if (!it.isNullOrEmpty()) {
                    hideNoConnectionText()
                }
            }
        }

        swipeRefreshLayout?.setOnRefreshListener {
            if (isFavoriteTab) {
                swipeRefreshLayout?.isRefreshing = false
            } else {
                viewModel.updateList(true)
            }
        }
        swipeRefreshLayout?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        if (savedInstanceState == null) {
            viewModel.updateList()
            if (viewModel.stocksList.value.isNullOrEmpty()) {
                showNoConnectionText()
            } else {
                hideNoConnectionText()
            }
        }

        viewModel.isFavSelected.observe(this.viewLifecycleOwner) { isFavSelected ->
            isFavSelected?.let {
                swapTab(it)
                isFavoriteTab = it
            }
        }

        tvStocks?.setOnClickListener {
            viewModel.swapToStocksTab()
        }

        tvFavorites?.setOnClickListener {
            viewModel.swapToFavTab()
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
}