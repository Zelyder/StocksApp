package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.viewModelFactoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SuggestionsFragment : Fragment() {
    @ExperimentalCoroutinesApi
    private val viewModel: SearchViewModel by viewModels {
        viewModelFactoryProvider()
            .viewModelFactory()
    }

    private var cgPopularRequests: ChipGroup? = null
    private var cgRecentlySearched: ChipGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestions, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cgPopularRequests = view.findViewById(R.id.cgPopularRequests)
        cgRecentlySearched = view.findViewById(R.id.cgRecentlySearched)

        viewModel.popularQueries.observe(this.viewLifecycleOwner) {
            it.forEach { item ->
                cgPopularRequests?.addView(createChip(item))
            }
        }

        viewModel.recentQueries.observe(this.viewLifecycleOwner) {
            it.forEach { item ->
                cgRecentlySearched?.addView(createChip(item))
            }
        }

        if (savedInstanceState == null) {
            viewModel.updatePopularQueries()
            viewModel.updateRecentQueries()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cgPopularRequests = null
        cgRecentlySearched = null
    }

    @ExperimentalCoroutinesApi
    private fun createChip(text: String): Chip {
        val chip = Chip(requireContext())
        chip.text = text
        chip.isClickable = true
        chip.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.search_container, SearchStocksListFragment.newInstance(text))
                .commit()
        }
        return chip

    }
}