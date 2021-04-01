package com.mmfinfotech.streameApp.utils

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * Created By Somil Rawal on Thu-24-12-2020-December-2020.
 */
class DividerItemDecoration(
    context: Context?,
    resId: Int
) : RecyclerView.ItemDecoration() {
    private val ATTRS = intArrayOf(R.attr.listDivider)
    private var divider: Drawable? = null

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left: Int = parent.paddingLeft
        val right: Int = parent.width - parent.paddingRight

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }

    init {
        if (resId == 0) {
            val styledAttributes: TypedArray? = context?.obtainStyledAttributes(ATTRS)
            divider = styledAttributes?.getDrawable(0)
            styledAttributes?.recycle()
        } else
            divider = ContextCompat.getDrawable(context!!, resId)
    }
}