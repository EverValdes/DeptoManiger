package com.ever.four.deptomaniger.adapter

import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.entity.ItemEntity
import com.ever.four.deptomaniger.helper.ItemTouchHelperAdapter
import com.ever.four.deptomaniger.helper.ItemTouchHelperViewHolder
import com.ever.four.deptomaniger.helper.OnStartDragListener
import kotlinx.android.synthetic.main.item.view.*
import java.util.*

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>, ItemTouchHelperAdapter {


    protected var list: List<ItemEntity>
    private val onStartDragListener: OnStartDragListener
    constructor(list: List<ItemEntity>, onStartDragListener: OnStartDragListener) {
        this.list = list
        this.onStartDragListener = onStartDragListener
    }

    inner class ViewHolder: RecyclerView.ViewHolder, ItemTouchHelperViewHolder {
        var card: CardView
        var name: TextView
        var owner: TextView
        var description: TextView
        //var currency: TextView
        var amount: TextView

        constructor(view: View): super(view) {
            //var position: Int = getAdapterPosition()
            card = view.card
            name = view.name
            owner = view.owner
            description = view.description
            //currency = view.currency
            amount = view.amount

        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemUnselected() {
            itemView.setBackgroundColor(Color.WHITE)
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
        }
        viewHolder.owner.text = list[i].owner
        viewHolder.amount.text = '$' + list[i].amount.toString()

        viewHolder.name.setOnTouchListener(View.OnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    onStartDragListener.onStartDrag(viewHolder)
                }
            }
            return@OnTouchListener false
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        list.drop(position)
        notifyItemRemoved(position)
    }
}