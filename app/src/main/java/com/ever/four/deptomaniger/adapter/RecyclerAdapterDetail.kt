package com.ever.four.deptomaniger.adapter

import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import com.ever.four.deptomaniger.R
import kotlinx.android.synthetic.main.detail_element.view.*

class RecyclerAdapterDetail: RecyclerView.Adapter<RecyclerAdapterDetail.ViewHolder> {
    var list: MutableList<String>
    constructor(list: MutableList<String>) {
        this.list = list
    }

    inner class ViewHolder: RecyclerView.ViewHolder {
        var startImage: ImageView
        var editContent: EditText

        constructor(detailView: View): super(detailView) {
            startImage = detailView.startImage
            editContent = detailView.editContent
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        var newDetail = LayoutInflater.from(viewGroup.context).inflate(R.layout.detail_element, viewGroup, false)
        newDetail.editContent.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addNewDetail("")
            }

            return@OnKeyListener true
        })

        return ViewHolder(newDetail)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.editContent.setText(list[i])
        if ((i == list.size -1) and (i != 0)) {
            viewHolder.editContent.requestFocus()
            viewHolder.editContent.setSelection(viewHolder.editContent.text.length)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun addNewDetail (text: String) {
        list.add(text)
        notifyDataSetChanged()
    }
}