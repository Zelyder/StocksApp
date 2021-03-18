package com.zelyder.stocksapp.presentation.searchscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zelyder.stocksapp.R

class SearchStocksListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_stocks_list, container, false)
    }

    companion object {
        //instance
    }
}