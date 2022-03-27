package com.twobros.itstore

import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QueriesTest {
    @Test
    fun `test query parsing`(){
        var test1 = "java"
        var queries1 = Queries(test1)

        Assert.assertEquals("java", queries1.query1)
        Assert.assertNull(queries1.query2)
        Assert.assertTrue(queries1.isValid)
        Assert.assertTrue(queries1.error.isEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "java|kotlin"
        queries1 = Queries(test1)

        Assert.assertEquals("java", queries1.query1)
        Assert.assertEquals("kotlin",queries1.query2)
        Assert.assertTrue(queries1.isValid)
        Assert.assertTrue(queries1.error.isEmpty())
        Assert.assertEquals(Queries.OP.OR, queries1.op )

        test1 = " java | kotlin "
        queries1 = Queries(test1)

        Assert.assertEquals("java", queries1.query1)
        Assert.assertEquals("kotlin",queries1.query2)
        Assert.assertTrue(queries1.isValid)
        Assert.assertTrue(queries1.error.isEmpty())
        Assert.assertEquals(Queries.OP.OR, queries1.op )

        test1 = "java-kotlin"
        queries1 = Queries(test1)

        Assert.assertEquals("java", queries1.query1)
        Assert.assertEquals("kotlin",queries1.query2)
        Assert.assertTrue(queries1.isValid)
        Assert.assertTrue(queries1.error.isEmpty())
        Assert.assertEquals(Queries.OP.NOT, queries1.op )

        test1 = " java - kotlin  "
        queries1 = Queries(test1)

        Assert.assertEquals("java", queries1.query1)
        Assert.assertEquals("kotlin",queries1.query2)
        Assert.assertTrue(queries1.isValid)
        Assert.assertTrue(queries1.error.isEmpty())
        Assert.assertEquals(Queries.OP.NOT, queries1.op )

        test1 = "java-kotlin|dfd"
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "java-kotlin|"
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "|java"
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "|-"
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "kkk |   "
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )

        test1 = "java | java  "
        queries1 = Queries(test1)

        Assert.assertFalse(queries1.isValid)
        Assert.assertTrue(queries1.error.isNotEmpty())
        Assert.assertEquals(Queries.OP.NA, queries1.op )
    }
}