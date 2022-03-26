package com.twobros.itstore

import com.twobros.itstore.repostory.api.BookStoreApiProvider
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BookStoreApiUnitTest {
    private val bookStoreApi = BookStoreApiProvider.bookStoreRxApi

    @Test
    fun `test search api`() {
        val search = bookStoreApi.search("mongo")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp ->
            resp.books[0].title.contains(
                "mongo",
                ignoreCase = true
            ) ?: false
        }
    }

    @Test
    fun `test search api with no result`() {
        val search = bookStoreApi.search("dfik1233#")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp ->
            resp.total.toInt() == 0
        }
    }

    @Test
    fun `test search api with page`() {
        val search = bookStoreApi.search("mongo", 1)
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp ->
            resp.books[0].title.contains(
                "mongo",
                ignoreCase = true
            ) ?: false
        }
    }

    @Test
    fun `test request book info api`() {
        val search = bookStoreApi.request("9781617294136")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp -> resp.title == "Securing DevOps" }
    }

    @Test
    fun `test request book info api with wrong id`() {
        val search = bookStoreApi.request("wrongid")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertError { ex -> ex is Exception }
    }

}