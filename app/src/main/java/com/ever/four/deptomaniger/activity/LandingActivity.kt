package com.ever.four.deptomaniger.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.adapter.RecyclerAdapter
import com.ever.four.deptomaniger.entity.ItemEntity
import kotlinx.android.synthetic.main.activity_landing.*
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.ever.four.deptomaniger.adapter.SimpleAdapter
import com.ever.four.deptomaniger.util.SwipeToDeleteCallback


class LandingActivity : AppCompatActivity(), View.OnClickListener {
    private val simpleAdapter = SimpleAdapter((1..5).map { "Item: $it" }.toMutableList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = simpleAdapter
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as SimpleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        addItemBtn.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addItemBtn -> simpleAdapter.addItem("New item")
        }
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
