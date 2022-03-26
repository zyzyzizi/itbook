package com.twobros.itstore

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.twobros.itstore.databinding.ActivityBookDetailBinding
import com.twobros.itstore.repostory.BookStoreRemoteRepository
import com.twobros.itstore.viewmodel.BookDetailViewModel
import com.twobros.itstore.viewmodel.BookDetailViewModelFactory

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private lateinit var detailViewModel: BookDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail)

        detailViewModel = ViewModelProvider(
            this,
            BookDetailViewModelFactory(application, intent, BookStoreRemoteRepository())
        )[BookDetailViewModel::class.java]

        with(detailViewModel) {
            isLoading.observe(this@BookDetailActivity) {
                with(binding) {
                    loadingProgress.visibility = View.VISIBLE
                    errorMessage.visibility = View.GONE
                    contentScrollview.visibility = View.GONE
                }
            }

            errorMessage.observe(this@BookDetailActivity) { msg ->
                with(binding) {
                    loadingProgress.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    contentScrollview.visibility = View.GONE

                    errorMessage.text = msg
                }
            }

            bookInfoLiveData.observe(this@BookDetailActivity) { bookInfo ->
                supportActionBar?.title = bookInfo.title

                with(binding) {
                    loadingProgress.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    contentScrollview.visibility = View.VISIBLE

                    Glide.with(bookCoverImage)
                        .load(bookInfo.image)
                        .placeholder(ColorDrawable(Color.GRAY))
                        .fitCenter()
                        .into(bookCoverImage)
                        .clearOnDetach()
                    title.itemName.text = "Title"
                    title.itemValue.text = bookInfo.title

                    author.itemName.text = "Author"
                    author.itemValue.text = bookInfo.authors

                    description.text = bookInfo.desc
                }
            }
        }

        detailViewModel.load()
    }
}