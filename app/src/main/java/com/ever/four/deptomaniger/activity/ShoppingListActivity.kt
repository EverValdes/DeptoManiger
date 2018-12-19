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
import com.ever.four.deptomaniger.util.BundleIdentifier
import kotlinx.android.synthetic.main.activity_shopping_list.*
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ever.four.deptomaniger.model.ItemViewModel
import com.google.firebase.auth.FirebaseAuth

class ShoppingListActivity : AppCompatActivity(), DragListener {


    private lateinit var recyclerAdapter: RecyclerAdapterList
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var viewModel: ItemViewModel? = null
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var authListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        if (it.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        setupHintText()
        setupAddItemBtn()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.single_item_menu, menu)
        var manageMenuItem: MenuItem = menu?.findItem(R.id.menu_item)!!

        manageMenuItem?.title = resources.getString(R.string.sign_off)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item -> {
                firebaseAuth.signOut()
            }
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            recyclerAdapter.setupElements(viewModel?.getDatumList()?.value!!)
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == BundleIdentifier.NEW_ITEM) {
                val result = data?.getSerializableExtra(BundleIdentifier.NEW_SHOP.Instance.toString())

                viewModel?.setDataToList(result as ItemEntity)
                //recyclerAdapter.addElement(result as ItemEntity)
            }
            if (requestCode == BundleIdentifier.MODIFIED_ITEM) {
                val result = data?.getSerializableExtra(BundleIdentifier.NEW_SHOP.Instance.toString())
                var index = 0
                index = data?.getIntExtra(BundleIdentifier.NEW_SHOP.Index.toString(), 0) as Int

                viewModel?.setDataToList(index, result as ItemEntity)
            }
        }
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }


    override fun onElementRemoved() {
        displayNoDataHint()
    }

    override fun onEditElement(intent: Intent) {
        startActivityForResult(intent, BundleIdentifier.MODIFIED_ITEM)
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

    private fun setupHintText() {
        noHintText.typeface = Typeface.createFromAsset(
            this.getAssets(),
            "font/Roboto-Light.ttf");
    }
    private fun setupAddItemBtn() {
        addItemBtn.setOnClickListener {
            Intent(this, AddShopActivity::class.java).also {
                startActivityForResult(it, BundleIdentifier.NEW_ITEM)
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
