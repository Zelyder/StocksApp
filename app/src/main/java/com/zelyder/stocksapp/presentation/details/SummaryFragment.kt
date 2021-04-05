package com.zelyder.stocksapp.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.viewModelFactoryProvider

class SummaryFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RatioListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvRatios)
        adapter = RatioListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.ratios.observe(this.viewLifecycleOwner) {
            adapter.bindList(it)
        }
        arguments?.getString(ARG_TICKER)?.let { viewModel.uploadRatios(it,resources) }
    }

    companion object {

        @JvmStatic
        fun newInstance(ticker: String) = SummaryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TICKER, ticker)
            }
        }
    }
}

private const val ARG_TICKER = "arg_ticker"