package com.ever.four.deptomaniger.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.entity.ItemEntity
import kotlinx.android.synthetic.main.activity_add_shop.*
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ArrayAdapter
import com.ever.four.deptomaniger.adapter.RecyclerAdapterDetail
import com.ever.four.deptomaniger.util.BundleIdentifier
import com.ever.four.deptomaniger.util.SharedPreferencesManager
import java.util.concurrent.CountDownLatch


class AddShopActivity : AppCompatActivity() {
    lateinit var manageMenuItem: MenuItem
    private lateinit var recyclerDetailAdapter: RecyclerAdapterDetail
    private lateinit var cacheManager: SharedPreferencesManager

    private val entitlementReady = CountDownLatch(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cacheManager = SharedPreferencesManager.getInstance(this)

        recyclerDetailView.layoutManager = LinearLayoutManager(this)

        fulfillFields()

        recyclerDetailView.adapter = recyclerDetailAdapter
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.single_item_menu, menu)
        manageMenuItem = menu?.findItem(R.id.menu_item)!!
        manageMenuItem?.title = resources.getString(R.string.save)
        //setupSaveButtonBehavior(manageMenuItem)
        manageMenuItem?.isEnabled = hasMandatoryFieldData()
        setupFieldsBehavior()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item -> {
                saveButtonTapped()
                return true
            }
            else -> {
                cancelButtonTapped()
                return false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }

    private fun saveButtonTapped() {
        val returnIntent = Intent()
        var newShop = ItemEntity(expenseName.text.toString(), shopperName.text.toString(), inputTotal.text.toString().toDouble())
        for (detailDescription in recyclerDetailAdapter.list) {
            if (!detailDescription.isNullOrBlank()) {
                newShop.description.add(detailDescription)
            }
        }
        saveNewName(shopperName.text.toString())
        returnIntent.putExtra(BundleIdentifier.NEW_SHOP.Instance.toString(), newShop)

        var extras = intent.extras
        extras?.let {
            var index = extras.get(BundleIdentifier.NEW_SHOP.Index.toString()) as Int
            returnIntent.putExtra(BundleIdentifier.NEW_SHOP.Index.toString(), index)
        }

        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun cancelButtonTapped() {
        if (hasAnyFieldData()) {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle(getString(R.string.error))
            alertDialogBuilder.setMessage(R.string.cancel_confirmation)
            alertDialogBuilder.setPositiveButton(getString(android.R.string.yes)) {_, _ ->
                cancelShop()
            }
            alertDialogBuilder.setNegativeButton(getString(android.R.string.no)) {_, _ ->

            }
            alertDialogBuilder.create().show()
        } else {
            cancelShop()
        }
    }

    private fun cancelShop() {
        val returnIntent = Intent()
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }

    private fun hasAnyFieldData(): Boolean {
        var filled = false
        var mandatoryFields = listOf<EditText?>(expenseName, shopperName, inputTotal)
        for (field in mandatoryFields) {
            if (!field?.text.isNullOrBlank()) {
                filled = true
                break
            }
        }

        if (recyclerDetailAdapter.list.size > 1 && recyclerDetailAdapter.list[0].length != 0 ) {
            filled = true
        }

        return filled
    }

    private fun hasMandatoryFieldData(): Boolean {
        var filled = true
        var mandatoryFields = listOf<EditText?>(expenseName, shopperName, inputTotal)
        for (field in mandatoryFields) {
            if (field?.text.isNullOrBlank()) {
                filled = false
                break
            }
        }
        return filled
    }

    private fun loadAutoSuggest() {
        var storeNames = cacheManager.getName()
        var arr = ArrayList<String>()
        var index = 0
        for (i in storeNames) {
            arr.add(index, i)
            index++
        }
        shopperName.threshold = 2
        shopperName.setAdapter(ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr))
    }

    private fun saveNewName(name: String) {
        cacheManager.putName(value = name)
    }

    private fun setupFieldsBehavior() {
        loadAutoSuggest()

        var mandatoryFieldListener = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val expenseName= expenseName.text
                val shopperName= shopperName.text
                val inputTotal = inputTotal.text

                val enable = expenseName.isNullOrBlank() or shopperName.isNullOrBlank() or inputTotal.isNullOrBlank()
                manageMenuItem.isEnabled = !enable
            }
        }
        expenseName.addTextChangedListener(mandatoryFieldListener)
        shopperName.addTextChangedListener(mandatoryFieldListener)
        inputTotal.addTextChangedListener(mandatoryFieldListener)
    }

    private fun fulfillFields() {
        var resultList = mutableListOf("")
        var extras = intent.extras
        extras?.let {
            var shop = extras.get(BundleIdentifier.NEW_SHOP.Instance.toString()) as ItemEntity
            resultList = shop.description

            expenseName.setText(shop.name)
            shopperName.setText(shop.shopper)
            inputTotal.setText(shop.amount.toString())
        } ?: run {
        }
        recyclerDetailAdapter = RecyclerAdapterDetail(resultList)
    }
}