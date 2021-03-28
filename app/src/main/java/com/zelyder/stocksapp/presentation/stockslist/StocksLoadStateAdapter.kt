package com.zelyder.stocksapp.presentation.stockslist

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class StocksLoadStateAdapter (private val retry: () -> Unit) : LoadStateAdapter<StocksLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: StocksLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StocksLoadStateViewHolder {
        return StocksLoadStateViewHolder.create(parent, retry)
    }
}