package com.twobros.itstore

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twobros.itstore.databinding.SearchActivityBinding
import com.twobros.itstore.repostory.BookStoreRepository
import com.twobros.itstore.repostory.api.model.IBook
import com.twobros.itstore.viewmodel.SearchViewModel
import com.twobros.itstore.viewmodel.SearchViewModel.Companion.NUM_ITEM_IN_PAGE
import com.twobros.itstore.viewmodel.SearchViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchActivityBinding

    private lateinit var menuSearch: MenuItem

    private var searchView: SearchView? = null

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModelFactory(application, BookStoreRepository())
        )[SearchViewModel::class.java]
    }

    private val searchRvAdapter = SearchAdapter()

    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_activity)

        initRecyclerView()

        observeData()
    }

    private fun observeData() {
        searchViewModel.isLoading.observe(this) {
            isLoading = it
            if (!it) {
                hideBottomProgress()
            }
        }
        searchViewModel.bookLiveData.observe(this) { books ->
            hideProgress()
            val start = searchRvAdapter.resultList.size
            searchRvAdapter.resultList.addAll(books)
            searchRvAdapter.notifyItemRangeInserted(start, books.size)

            if (searchRvAdapter.resultList.size < NUM_ITEM_IN_PAGE) {
                searchViewModel.load()
            }
        }

        searchViewModel.errorMessage.observe(this) {
            if (it == null) {
                hideErrorMessage()
            } else {
                showErrorMessage(it)
            }
        }
    }

    private fun initRecyclerView() {
        binding.resultListView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchRvAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val last = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (searchRvAdapter.resultList.size <= last + THREAD_HOLD && !isLoading) {
                        if (searchViewModel.load()){
                            showBottomProgress()
                        }
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        menuSearch = menu.findItem(R.id.menu_activity_search_query)
        searchView = (menuSearch.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i(TAG, "onQueryTextSubmit:  $query")
                    process(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
        menuSearch.expandActionView()
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun process(query: String) {
        updateTitle(query)
        hideKeyboard()
        closeSearchView()
        searchRvAdapter.resultList.clear()
        searchRvAdapter.notifyDataSetChanged()

        val queries = Queries(query)

        if (queries.isValid) {
            searchViewModel.init(queries)
            searchViewModel.load()
            showProgress()
        } else {
            showErrorMessage(queries.error)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_activity_search_query) {
            item.expandActionView()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun closeSearchView() {
        menuSearch.collapseActionView()
    }

    private fun updateTitle(query: String) {
        supportActionBar?.run { subtitle = query }
    }

    private fun hideKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(searchView?.windowToken, 0)
        }
    }

    private fun showProgress() {
        binding.resultListView.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.loadingProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.loadingProgress.visibility = View.GONE
        binding.resultListView.visibility = View.VISIBLE
    }

    private fun showErrorMessage(msg: String) {
        binding.loadingProgress.visibility = View.GONE
        binding.resultListView.visibility = View.GONE
        binding.errorMessage.apply {
            visibility = View.VISIBLE
            text = msg
        }
    }

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = View.GONE
    }

    private fun showBottomProgress() {
        if (searchRvAdapter.resultList[searchRvAdapter.resultList.size - 1] is LoadingItem) {
            return
        }
        searchRvAdapter.resultList.add(loadingItem)
        searchRvAdapter.notifyItemInserted(searchRvAdapter.resultList.size - 1)
    }

    private fun hideBottomProgress() {
        searchRvAdapter.resultList.remove(loadingItem)
        searchRvAdapter.notifyItemRemoved(searchRvAdapter.resultList.size)
    }

    class LoadingItem : IBook

    private val loadingItem: LoadingItem = LoadingItem()

    companion object {
        private val TAG = "shk-${SearchActivity::class.java.simpleName}"
        private const val THREAD_HOLD = 3
    }
}