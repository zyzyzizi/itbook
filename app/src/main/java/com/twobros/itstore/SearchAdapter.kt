package com.twobros.itstore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twobros.itstore.repostory.api.model.Book
import com.twobros.itstore.repostory.api.model.IBook

class SearchAdapter : RecyclerView.Adapter<SearchItemViewHolder>() {
    var resultList = ArrayList<IBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        when (ViewType.of(viewType)) {
            ViewType.RESULT -> SearchItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.result_card_item, parent, false)
            )
            ViewType.LOADING -> SearchItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            )
        }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(resultList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (resultList[position] is Book) ViewType.RESULT.type else ViewType.LOADING.type
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}