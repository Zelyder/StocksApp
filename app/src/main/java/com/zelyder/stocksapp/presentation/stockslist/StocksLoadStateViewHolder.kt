package com.zelyder.stocksapp.presentation.stockslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R

class StocksLoadStateViewHolder (
    itemView: View,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val retryButton: Button = itemView.findViewById(R.id.btnRetryFooter)
    private val tvErrorFooter: TextView = itemView.findViewById(R.id.tvErrorFooter)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarFooter)

    init {
        retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            tvErrorFooter.text = loadState.error.localizedMessage
        }
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        tvErrorFooter.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): StocksLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer_item, parent, false)
            return StocksLoadStateViewHolder(view, retry)
        }
    }
}