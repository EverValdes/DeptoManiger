package com.ever.four.deptomaniger.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.ever.four.deptomaniger.R
import kotlinx.android.synthetic.main.detail_container.view.*
import kotlinx.android.synthetic.main.detail_element.view.*

class DetailContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var previousChild: View
    private var listener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
            newDetail()
        }

        return@OnKeyListener true
    }
    init {
        LayoutInflater.from(context).inflate(R.layout.detail_container, this, true)
        previousChild = firstDetail
        previousChild.editContent.setOnKeyListener(listener)
    }


    private fun newDetail() {
        var newDetail= LayoutInflater.from(context).inflate(R.layout.detail_element, this, true)
        newDetail.editContent.setOnKeyListener(listener)
        newDetail.editContent.setText("nuevo texto")
        newDetail.background = ColorDrawable(android.graphics.Color.RED)
        newDetail.id = View.generateViewId()

        var set = ConstraintSet()
        set.clone(detailContainer)

        set.connect(newDetail.id, ConstraintSet.TOP, previousChild.id, ConstraintSet.BOTTOM, 100)
        set.applyTo(detailContainer)
        previousChild = newDetail
       // this.addView(newDetail)
    }
}