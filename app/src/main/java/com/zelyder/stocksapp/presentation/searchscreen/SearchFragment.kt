package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.presentation.core.hideKeyboard
import com.zelyder.stocksapp.presentation.core.showKeyboard


class SearchFragment : Fragment() {


    private var cgPopularRequests: ChipGroup? = null
    private var cgRecentlySearched: ChipGroup? = null
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cgPopularRequests = view.findViewById(R.id.cgPopularRequests)
        cgRecentlySearched = view.findViewById(R.id.cgRecentlySearched)
        searchView = view.findViewById(R.id.main_searchView)
        searchView?.setOnClickListener {
            findNavController().navigateUp()
        }
        searchView?.setOnQueryTextFocusChangeListener { sView, hasFocus ->
            if (hasFocus) {
                sView.showKeyboard()
            }
            else {
                sView.hideKeyboard()
            }
        }

        searchView?.requestFocus()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        cgPopularRequests = null
        cgRecentlySearched = null
        searchView = null
    }

}