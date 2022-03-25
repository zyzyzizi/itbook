package com.twobros.itstore

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

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