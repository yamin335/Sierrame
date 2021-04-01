package com.mmfinfotech.streameApp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OverlapDecoration: RecyclerView.ItemDecoration() {
    companion object {
        private const val vertOverlap = -30
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        outRect.set(0, 0, vertOverlap, 0)
    }
}



