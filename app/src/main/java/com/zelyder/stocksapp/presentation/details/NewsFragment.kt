package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.viewModelFactoryProvider

class NewsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvNews)
        adapter = NewsListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.news.observe(this.viewLifecycleOwner) {
            adapter.bindList(it)
        }
        arguments?.getString(ARG_TICKER)?.let { viewModel.uploadNews(it) }
    }


    companion object {

        @JvmStatic
        fun newInstance(ticker: String) = NewsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TICKER, ticker)
            }
        }
    }
}

private const val ARG_TICKER = "arg_ticker"