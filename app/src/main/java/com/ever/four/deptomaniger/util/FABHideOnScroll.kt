package com.ever.four.deptomaniger.util

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

class FABHideOnScroll: FloatingActionButton.Behavior {
    constructor(): super()
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        var ret: Boolean
        if (nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            ret = true
        } else {
            ret = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type)
        }

        return ret
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        /* If RecyclerView scroll action consumed vertical pixels bigger than 0, means scroll down. */
        if (dyConsumed > 0) {
            if (child.visibility == View.VISIBLE) {

                child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                    @SuppressLint("RestrictedApi")
                    override fun onHidden(floatingActionButton: FloatingActionButton?) {
                        super.onHidden(floatingActionButton)
                        floatingActionButton?.visibility = View.INVISIBLE
                    }
                })
            }
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}