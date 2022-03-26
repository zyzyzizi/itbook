package com.twobros.itstore

class Queries(query: String) {
    sealed class OP(val ch: String) {
        object NA : OP("")
        object OR : OP("|")
        object NOT : OP("-")
    }

    var query1: String?
    var query2: String?
    val op: OP
    val isValid: Boolean
    val error: String

    init {
        val tokens = query.trim().split(SPLIT_REG)
        if (tokens.size !in 1..2) {
            isValid = false
            error = "invalid query (count:${tokens.size})"
            query1 = null
            query2 = null
            op = OP.NA
        } else {
            if (tokens.size == 1) {
                query1 = tokens[0].trim()
                query2 = null
                op = OP.NA
                error = ""
                isValid = true
            } else {
                //case 2
                query1 = tokens[0].trim()
                query2 = tokens[1].trim()
                if (query1?.isEmpty() == true || query2?.isEmpty() == true || query1 == query2) {
                    isValid = false
                    error = "invalid query"
                    op = OP.NA
                } else {
                    error = ""
                    isValid = true
                    val orIdx = query.indexOf(OP.OR.ch)

                    op = if (orIdx == -1) {
                        OP.NOT
                    } else {
                        OP.OR
                    }
                }
            }
        }
    }

    companion object {
        private val SPLIT_REG = "[|-]".toRegex()
    }
}