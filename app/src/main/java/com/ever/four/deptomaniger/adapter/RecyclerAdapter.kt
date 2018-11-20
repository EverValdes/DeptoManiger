package com.ever.four.deptomaniger.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.entity.ItemEntity
import kotlinx.android.synthetic.main.item.view.*

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    protected var list: List<ItemEntity>

    constructor(list: List<ItemEntity>) {
        this.list = list
    }

    inner class ViewHolder: RecyclerView.ViewHolder {
        var name: TextView
        var owner: TextView
        var description: TextView
        //var currency: TextView
        var amount: TextView
        constructor(view: View): super(view) {
            var position: Int = getAdapterPosition()
            name = view.name
            owner = view.owner
            description = view.description
            //currency = view.currency
            amount = view.amount

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.name.text = list[i].name
        list[i].description?.let {
            viewHolder.description.text = it
        }?: run {
            viewHolder.description.visibility = View.GONE
        }
        viewHolder.owner.text = list[i].owner
        viewHolder.amount.text = '$' + list[i].amount.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}