package com.zelyder.stocksapp.presentation.details

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zelyder.stocksapp.R
import com.zelyder.stocksapp.domain.models.News


class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    var newsList: List<News> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun bindList(newNewsList: List<News>) {
        newsList = newNewsList
        notifyDataSetChanged()
    }


    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivNews: ImageView = itemView.findViewById(R.id.ivNews)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitleNews)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContentNews)

        fun bind(news: News) {
            tvTitle.text = news.title
            tvContent.text = news.text

            if (news.image.isNotEmpty()) {
                Picasso.get().load(news.image)
                    .placeholder(R.drawable.ic_image)
                    .into(ivNews)
            } else {
                ivNews.visibility = View.GONE
            }

            itemView.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(news.url)
                itemView.context.startActivity(intent)
            }

        }
    }
}