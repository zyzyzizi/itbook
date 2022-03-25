package com.twobros.itstore

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.twobros.itstore.databinding.ResultItemBinding
import com.twobros.itstore.repostory.api.model.Book
import com.twobros.itstore.repostory.api.model.IBook
import com.twobros.itstore.viewmodel.BookDetailViewModel

class SearchAdapter : RecyclerView.Adapter<SearchItemViewHolder>() {
    var resultList = ArrayList<IBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder =
        when (ViewType.of(viewType)) {
            ViewType.RESULT -> SearchItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
            )
            ViewType.LOADING -> SearchItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            )
        }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val item  = resultList[position]
        if (item is Book) {
            DataBindingUtil.bind<ResultItemBinding>(holder.itemView)?.let { binding ->
                Glide.with(binding.bookImage)
                    .load(item.image)
                    .placeholder(ColorDrawable(Color.GRAY))
                    .fitCenter()
                    .into(binding.bookImage)
                    .clearOnDetach()
                binding.bookTitle.text = item.title
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(it!!.context, BookDetailActivity::class.java).apply {
                    putExtra(BookDetailViewModel.KEY_ISBN, item.isbn13)
                }
                it.context.startActivity(intent)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (resultList[position] is Book) ViewType.RESULT.type else ViewType.LOADING.type
    }

    override fun getItemCount(): Int {
        return resultList.size
    }


}