package com.zelyder.stocksapp.presentation.stockslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock

class StocksListAdapter : RecyclerView.Adapter<StocksListAdapter.StocksViewHolder>() {

    private var stocks = listOf<Stock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        return StocksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stock, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int = stocks.size

    fun bindStocks(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }

    inner class StocksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTicker: TextView = itemView.findViewById(R.id.tvTicker)
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tvCompanyName)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)
        private val tvDayDelta: TextView = itemView.findViewById(R.id.tvDayDelta)
        private val ivLogo: ImageView = itemView.findViewById(R.id.ivLogo)
        private val ivFav: ImageView = itemView.findViewById(R.id.ivFav)

        fun bind(stock: Stock) {
            tvTicker.text = stock.ticker
            tvCompanyName.text = stock.companyName
            tvCurrentPrice.text =
                itemView.resources.getString(
                    when (stock.currency) {
                        "ru" -> R.string.currency_ru
                        "eng" -> R.string.currency_eng
                        else -> R.string.currency_eng
                    },
                    stock.price.toString()
                )

            tvDayDelta.text = stock.dayDelta.toString()

            if (stock.logo.isNotEmpty()) {
                Picasso.get().load(stock.logo)
                    .into(ivLogo)
            }

            ivFav.setImageResource(
                when (stock.isFavorite) {
                    true -> R.drawable.ic_fav_active
                    false -> R.drawable.ic_fav_none
                }
            )
        }
    }
}