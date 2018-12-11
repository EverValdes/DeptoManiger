package com.ever.four.deptomaniger.helper

import android.support.v7.widget.RecyclerView

interface DragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

    fun onElementRemoved()
}