package com.zelyder.stocksapp.presentation.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.Ratio
import okhttp3.internal.immutableListOf

class RatioListAdapter : RecyclerView.Adapter<RatioListAdapter.RatioViewHolder>() {
    private var ratios: List<Ratio> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatioViewHolder {
        return RatioViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ratio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RatioViewHolder, position: Int) {
        holder.bind(ratios[position])
    }

    override fun getItemCount(): Int = ratios.size

    fun bindList(newRatios: List<Ratio>) {
        ratios = newRatios
        notifyDataSetChanged()
    }

    inner class RatioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvRatio: TextView = itemView.findViewById(R.id.tvRatio)
        private val tvRatioExp: TextView = itemView.findViewById(R.id.tvRatioExp)
        private val tvRatioVal: TextView = itemView.findViewById(R.id.tvRatioVal)

        fun bind(ratio: Ratio) {
            tvRatio.text = ratio.name
            tvRatioExp.text = ratio.description
            tvRatioVal.text = ratio.value.toString()
        }
    }
}