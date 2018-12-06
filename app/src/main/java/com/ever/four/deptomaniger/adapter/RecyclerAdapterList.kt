package com.ever.four.deptomaniger.adapter

import android.support.constraint.ConstraintLayout
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
import kotlinx.android.synthetic.main.new_item.view.*
import java.util.*

class RecyclerAdapterList: RecyclerView.Adapter<RecyclerAdapterList.ViewHolder>, ItemTouchHelperAdapter {


    private var list: MutableList<ItemEntity>
    private val onStartDragListener: OnStartDragListener
    constructor(list: MutableList<ItemEntity>, onStartDragListener: OnStartDragListener) {
        this.list = list
        this.onStartDragListener = onStartDragListener
    }

    inner class ViewHolder: RecyclerView.ViewHolder, ItemTouchHelperViewHolder {
        var containerDescription: ConstraintLayout
        var name: TextView
        var shopper: TextView
        var description: TextView
        //var currency: TextView
        var amount: TextView

        constructor(view: View): super(view) {
            //var position: Int = getAdapterPosition()
            containerDescription = view.content
            name = view.name
            shopper = view.shopper
            description = view.description
            //currency = view.currency
            amount = view.amount

        }

        override fun onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemUnselected() {
            //itemView.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.new_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.name.text = list[i].name
        list[i].description?.let {
            viewHolder.containerDescription.visibility = View.VISIBLE
            viewHolder.description.text = it
        }
        viewHolder.shopper.text = list[i].shopper
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
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addNew(element: ItemEntity) {
        list.add(element)
        notifyDataSetChanged()
    }
}