package com.twobros.itstore

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twobros.itstore.databinding.ResultCardItemBinding
import com.twobros.itstore.repostory.api.model.Book
import com.twobros.itstore.repostory.api.model.IBook
import com.twobros.itstore.viewmodel.BookDetailViewModel

class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: IBook) {
        if (item is Book) {
            DataBindingUtil.bind<ResultCardItemBinding>(itemView)?.let { binding ->
                Glide.with(binding.bookImage)
                    .load(item.image)
                    .placeholder(ColorDrawable(Color.GRAY))
                    .fitCenter()
                    .into(binding.bookImage)
                    .clearOnDetach()
                binding.bookTitle.text = item.title
            }

            itemView.setOnClickListener {
                val intent = Intent(it!!.context, BookDetailActivity::class.java).apply {
                    putExtra(BookDetailViewModel.KEY_ISBN, item.isbn13)
                }
                it.context.startActivity(intent)
            }
        }
        // else is loading card
    }
}

sealed class ViewType(val type: Int) {
    object RESULT : ViewType(0)
    object LOADING : ViewType(1)

    companion object {
        fun of(type: Int): ViewType = when (type) {
            0 -> RESULT
            1 -> LOADING
            else -> throw IllegalArgumentException()
        }
    }
}