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
import com.ever.four.deptomaniger.helper.DragListener
import com.ever.four.deptomaniger.helper.ItemTouchHelperCallback
import com.ever.four.deptomaniger.util.ActivityResult
import kotlinx.android.synthetic.main.activity_landing.*
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.ever.four.deptomaniger.model.ItemViewModel

class LandingActivity : AppCompatActivity(), DragListener {
    private lateinit var recyclerAdapter: RecyclerAdapterList
    private lateinit var itemTouchHelper: ItemTouchHelper

    private var viewModel: ItemViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        recyclerAdapter = RecyclerAdapterList(emptyList<ItemEntity>().toMutableList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        viewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        viewModel?.getDatumList()?.observe(this, Observer {
            displayNoDataHint()
            recyclerAdapter.setupElements(it!!)
        })

        val callback = ItemTouchHelperCallback(recyclerAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = recyclerAdapter

        displayNoDataHint()
        setupAddItemBtn()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ActivityResult.NEW_ITEM) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getSerializableExtra("result")

                viewModel?.addDataToList(result as ItemEntity)
                //recyclerAdapter.addElement(result as ItemEntity)
            }
            if (resultCode == Activity.RESULT_CANCELED) {}
        }
    }

    override fun onElementRemoved() {
        displayNoDataHint()
    }

    fun mockData(): LiveData<MutableList<ItemEntity>> {
        var item1 = ItemEntity("Compra super", "Ever", 2.0)

        var item2 = item1.clone()
        item2.name = "Pan"

        var item3 = item1.clone()
        item3.description =  mutableListOf("Leche", "Carne", "Aceite")
        item3.shopper = "Javier"
        item3.amount = 5.0

        var itemLists : MutableList<ItemEntity> = emptyList<ItemEntity>().toMutableList()
        itemLists.add(item1)
        itemLists.add(item2)
        itemLists.add(item3)

        var elements= MutableLiveData<MutableList<ItemEntity>>()!!
        elements.value = itemLists
        return elements
    }

    private fun setupAddItemBtn() {
        addItemBtn.setOnClickListener {
            Intent(this, AddShopActivity::class.java).also {
                startActivityForResult(it, ActivityResult.NEW_ITEM)
            }
        }
    }

    private fun displayNoDataHint() {
        val empty = ((viewModel?.getDatumList()?.value?.size == 0) or (viewModel?.getDatumList()?.value.isNullOrEmpty()))
        displayNoDataHint(empty)
    }

    private fun displayNoDataHint(empty: Boolean) {
        if (empty) {
            noDataHint.visibility = View.VISIBLE
        } else {
            noDataHint.visibility = View.GONE
        }
    }
}
