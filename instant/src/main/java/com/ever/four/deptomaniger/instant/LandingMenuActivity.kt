package com.ever.four.deptomaniger.instant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


import entity.ItemList
import controller.SwipeController
import adapter.ItemRecyclerDataAdapter
import android.graphics.Canvas
import android.support.v7.widget.helper.ItemTouchHelper
import controller.SwipeControllerActions
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


class LandingMenuActivity : AppCompatActivity() {
    lateinit var adapterRecycler: ItemRecyclerDataAdapter
    val items: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //addItems()
        setupRecyclerView()



        //activity_main.expense_list.layoutManager = LinearLayoutManager(this)
        //activity_main.expense_list.adapterRecycler = RecyclerViewAdapter(items, this)
    }

    private fun setItemDataAdapter() {
        var item1 = ItemList("Leche", "Ever", "description", 2)

        var item2 = item1.clone()
        item2.name = "Pan"

        var item3 = item1.clone()
        item3.description = "nueva descripcion"
        item3.owner = "Juan"
        item3.amount = 5

        var itemLists : MutableList<ItemList> = ArrayList()
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

        adapterRecycler = ItemRecyclerDataAdapter(itemLists)
    }

    private fun setupRecyclerView() {
        recyclerView.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        setItemDataAdapter()
        recyclerView.setAdapter(adapterRecycler)
        var swipeController = SwipeController(object: SwipeControllerActions() {
            override fun onEditClicked(position: Int) {

            }
            override fun onDeleteClicked(position: Int) {
                adapterRecycler.notifyItemRemoved(position)
                adapterRecycler.itemLists.removeAt(position)
            }
        })

        var itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            @Override
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }
}
