package com.zelyder.stocksapp.presentation.stockslist

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Stock
import com.zelyder.stocksapp.presentation.core.toDeltaString
import com.zelyder.stocksapp.presentation.core.toPriceString
import com.zelyder.stocksapp.presentation.details.DetailsFragmentDirections


class StocksListAdapter(private val itemClickListener: StockListItemClickListener) : RecyclerView.Adapter<StocksListAdapter.StocksViewHolder>() {

    private var stocks = listOf<Stock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksViewHolder {
        return StocksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stock, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StocksViewHolder, position: Int) {
        if (position % 2 != 0) {
            val typedValue = TypedValue()
            holder.cvItem.context.theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
            holder.cvItem.setCardBackgroundColor(typedValue.data)
        }else {
            val typedValue = TypedValue()
            holder.cvItem.context.theme.resolveAttribute(R.attr.cardBackgroundColor, typedValue, true)
            holder.cvItem.setCardBackgroundColor(typedValue.data)
        }
        holder.bind(stocks[position])


    }

    override fun getItemCount(): Int = stocks.size

    fun bindStocks(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }

    inner class StocksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvItem: CardView = itemView.findViewById(R.id.cvItem)
        private val tvTicker: TextView = itemView.findViewById(R.id.tvTicker)
        private val tvCompanyName: TextView = itemView.findViewById(R.id.tvCompanyName)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)
        private val tvDayDelta: TextView = itemView.findViewById(R.id.tvDayDelta)
        private val ivLogo: ImageView = itemView.findViewById(R.id.ivLogo)
        private val ivFav: ImageView = itemView.findViewById(R.id.ivFav)

        fun bind(stock: Stock) {
            tvTicker.text = stock.ticker
            tvCompanyName.text = stock.companyName

            tvCurrentPrice.text = toPriceString(stock.price, stock.currency, itemView.resources)
            tvDayDelta.text = toDeltaString(
                stock.dayDelta,
                stock.dayDeltaPercent,
                stock.currency,
                itemView.resources
            )
            if (stock.dayDelta < 0) {
                tvDayDelta.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
            } else {
                tvDayDelta.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            }

            if (stock.logo.isNotEmpty()) {
                Picasso.get().load(stock.logo)
                    .placeholder(R.drawable.ic_image)
                    .into(ivLogo)
            }

            switchFav(stock.isFavorite)

            cvItem.setOnClickListener {
                it.findNavController().navigate(DetailsFragmentDirections.actionGlobalDetailsFragment(stock))
            }

            ivFav.setOnClickListener {
                stock.isFavorite = !stock.isFavorite
                switchFav(stock.isFavorite)
                itemClickListener.onClickFavourite(stock)
            }
        }

        private fun switchFav(isFavorite: Boolean) {
            ivFav.setImageResource(
                when (isFavorite) {
                    true -> R.drawable.ic_fav_active
                    false -> R.drawable.ic_fav_none
                }
            )
        }
    }
}