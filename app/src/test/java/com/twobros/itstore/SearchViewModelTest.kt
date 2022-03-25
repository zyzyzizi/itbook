package com.twobros.itstore

import android.os.Build
import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.twobros.itstore.repostory.BookStoreRepository
import com.twobros.itstore.repostory.api.BookStoreApiProvider
import com.twobros.itstore.repostory.api.model.Book
import com.twobros.itstore.viewmodel.SearchViewModel
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [28])
class SearchViewModelTest {


    private fun makeMockProvider(): BookStoreRepository {
        return mockk(relaxed = true)
    }

    private fun makeBookStub(title: String): Book = Book("", "", "", "", title, "")

    @Test
    fun `test updateResultList`() {
        val test1 = "java"
        val queries1 = Queries(test1)

        val searchViewModel = SearchViewModel(makeMockProvider()).apply {
            init(queries1)
        }
        val booklist = ArrayList<Book>()
        val books = listOf(makeBookStub("aa"), makeBookStub("bb"), makeBookStub("aa bb"))
        booklist.addAll(books)

        searchViewModel.updateResultList(books)

        shadowOf(Looper.getMainLooper()).idle()

        Assert.assertEquals(books, searchViewModel.bookLiveData.value)
        Assert.assertTrue(booklist.containsAll(searchViewModel.bookList))

        val books1 = listOf(makeBookStub("1"), makeBookStub("2"), makeBookStub("3 4"))
        booklist.addAll(books1)
        searchViewModel.updateResultList(books1)
        Assert.assertEquals(books1, searchViewModel.bookLiveData.value)
        Assert.assertTrue(booklist.containsAll(searchViewModel.bookList))
    }

    @Test
    fun `test updateResultList for NOT`() {
        val test1 = "java-kotlin"
        val queries1 = Queries(test1)

        val searchViewModel = SearchViewModel(makeMockProvider()).apply {
            init(queries1)
        }

        val books =
            listOf(makeBookStub("java aa"), makeBookStub("java bb"), makeBookStub("java kotlin"))

        searchViewModel.updateResultList(books)

        shadowOf(Looper.getMainLooper()).idle()

        Assert.assertTrue(searchViewModel.bookLiveData.value?.contains(makeBookStub("java kotlin")) == false)
        Assert.assertFalse(searchViewModel.bookList.contains(makeBookStub("java kotlin")))
    }
}