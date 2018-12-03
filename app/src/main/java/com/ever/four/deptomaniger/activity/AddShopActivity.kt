package com.ever.four.deptomaniger.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import com.ever.four.deptomaniger.R
import com.ever.four.deptomaniger.entity.ItemEntity
import kotlinx.android.synthetic.main.activity_add_shop.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.TextInputEditText
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View


class AddShopActivity : AppCompatActivity() {
    lateinit var manageMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)

        setupFieldsBehavior()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.single_item_menu, menu)
        manageMenuItem = menu?.findItem(R.id.menu_item)!!

        manageMenuItem?.title = resources.getString(R.string.save)
        setupSaveButtonBehavior(manageMenuItem)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.menu_item) {
            var newShop = ItemEntity(expenseName.text.toString(), shopperName.text.toString(), inputTotal.text.toString().toDouble())
            if (!inputList.text.isNullOrBlank()) {
                newShop.description = inputList.text.toString()
            }
            val returnIntent = Intent()
            returnIntent.putExtra("result", newShop)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun retrieveMandatoryFields(): List<TextInputEditText?> {
        return listOf<TextInputEditText?>(expenseName, shopperName, inputTotal)
    }

    private fun setupSaveButtonBehavior(menu: MenuItem?) {
        var mandatoryFields = retrieveMandatoryFields()

        var enableSaveButton = true
        for (field in mandatoryFields) {
            if (field?.text.isNullOrBlank()) {
                enableSaveButton = false
                break
            }
        }

        menu?.isEnabled = enableSaveButton
    }

    private fun setupFieldsBehavior() {
        var listener = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val expenseName= expenseName.text
                val shopperName= shopperName.text
                val inputTotal = inputTotal.text

                val enable = expenseName.isNullOrBlank() or shopperName.isNullOrBlank() or inputTotal.isNullOrBlank()
                manageMenuItem.isEnabled = !enable
            }
        }
        expenseName.addTextChangedListener(listener)
        shopperName.addTextChangedListener(listener)
        inputTotal.addTextChangedListener(listener)

        /*val bulletPoint = getString(R.string.bullet_point)
        inputList.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (inputList.text.isNullOrBlank()) {
                    inputList.text = Editable.Factory.getInstance().newEditable(bulletPoint)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })*/
    }
}
