package com.ever.four.deptomaniger.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.adapter.RecyclerAdapter
import com.ever.four.deptomaniger.entity.ItemEntity
import com.ever.four.deptomaniger.helper.OnStartDragListener
import com.ever.four.deptomaniger.helper.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_landing.*


class LandingActivity : AppCompatActivity(), OnStartDragListener {
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter(mockData(), this)


        val callback = SimpleItemTouchHelperCallback(recyclerAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = recyclerAdapter
    }



    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    fun mockData(): List<ItemEntity>{
        var item1 = ItemEntity("Leche", "Ever", 2)

        var item2 = item1.clone()
        item2.name = "Pan"

        var item3 = item1.clone()
        item3.description = "nueva descripcion"
        item3.owner = "Juan"
        item3.amount = 5

        var itemLists : MutableList<ItemEntity> = ArrayList()
        itemLists.add(item1)
        itemLists.add(item2)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item1)
        itemLists.add(item2)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)
        itemLists.add(item3)

        return itemLists
    }
}
