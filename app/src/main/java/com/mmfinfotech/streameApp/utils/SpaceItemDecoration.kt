package com.mmfinfotech.streameApp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By Somil Rawal on Wed-25-November-2020.
 */
class SpaceItemDecoration(
    val horizontalSpacing: Int,
    val verticalSpacing: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontalSpacing
        outRect.left = horizontalSpacing

//        if (parent.getChildLayoutPosition(view) == 0)
        outRect.top = verticalSpacing
        outRect.bottom = verticalSpacing
    }
}