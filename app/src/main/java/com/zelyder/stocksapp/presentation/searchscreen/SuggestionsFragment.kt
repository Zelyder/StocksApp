package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.ChipGroup
import com.zelyder.stocksapp.R

class SuggestionsFragment : Fragment() {

    private var cgPopularRequests: ChipGroup? = null
    private var cgRecentlySearched: ChipGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cgPopularRequests = view.findViewById(R.id.cgPopularRequests)
        cgRecentlySearched = view.findViewById(R.id.cgRecentlySearched)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cgPopularRequests = null
        cgRecentlySearched = null
    }
}