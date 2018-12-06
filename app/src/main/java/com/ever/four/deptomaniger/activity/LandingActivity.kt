package com.ever.four.deptomaniger.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.adapter.RecyclerAdapterList
import com.ever.four.deptomaniger.entity.ItemEntity
import com.ever.four.deptomaniger.helper.OnStartDragListener
import com.ever.four.deptomaniger.helper.ItemTouchHelperCallback
import com.ever.four.deptomaniger.util.ActivityResult
import kotlinx.android.synthetic.main.activity_landing.*
import android.app.Activity






class LandingActivity : AppCompatActivity(), OnStartDragListener {
    private lateinit var recyclerAdapter: RecyclerAdapterList
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapterList(mockData(), this)


        val callback = ItemTouchHelperCallback(recyclerAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = recyclerAdapter

        setupAddItemBtn()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ActivityResult.NEW_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getSerializableExtra("result")
                recyclerAdapter.addNew(result as ItemEntity)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    fun mockData(): MutableList<ItemEntity>{
        var item1 = ItemEntity("Compra super", "Ever", 2.0)

        var item2 = item1.clone()
        item2.name = "Pan"

        var item3 = item1.clone()
        item3.description =  "Leche" + "\n" + "Carne" + "\n" + "aceite"
        item3.shopper = "Juan"
        item3.amount = 5.0

        var itemLists : MutableList<ItemEntity> = ArrayList()
        itemLists.add(item1)
        itemLists.add(item2)
        itemLists.add(item3)

        return itemLists
    }

    private fun setupAddItemBtn() {
        addItemBtn.setOnClickListener {
            Intent(this, AddShopActivity::class.java).also {
                startActivityForResult(it, ActivityResult.NEW_ITEM)
            }
        }
    }
}
