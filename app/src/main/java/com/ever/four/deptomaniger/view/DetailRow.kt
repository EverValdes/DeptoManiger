package com.ever.four.deptomaniger.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.*
import com.ever.four.deptomaniger.R
import kotlinx.android.synthetic.main.detail_element.view.*

class DetailRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    parent: ConstraintLayout
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        var newDetailRow = LayoutInflater.from(context).inflate(R.layout.detail_element, this, true)

        editContent.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                //Perform Code
                parent.addView(newDetailRow)
            }

            return@OnKeyListener true
        })
    }

}
