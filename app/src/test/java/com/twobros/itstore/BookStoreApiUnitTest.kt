package com.twobros.itstore

import com.twobros.itstore.repostory.api.bookStoreApi
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BookStoreApiUnitTest {
    @Test
    fun `test search api`() {
        val search = bookStoreApi.search("mongo")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp ->
            resp.body()?.books?.get(0)?.title?.contains(
                "mongo",
                ignoreCase = true
            ) ?: false
        }
    }

    @Test
    fun `test search api with page`() {
        val search = bookStoreApi.search("mongo", 1)
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp ->
            resp.body()?.books?.get(0)?.title?.contains(
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
        search.assertValue { resp -> resp.body()?.title == "Securing DevOps" }
    }

    @Test
    fun `test request book info api with wrong id`() {
        val search = bookStoreApi.request("wrongid")
            .test()
        search.awaitDone(10, TimeUnit.SECONDS)
        search.assertValue { resp -> !resp.isSuccessful }
    }
}