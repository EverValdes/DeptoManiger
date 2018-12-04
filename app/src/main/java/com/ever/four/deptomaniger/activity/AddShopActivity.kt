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
import android.content.Intent
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ArrayAdapter
import com.ever.four.deptomaniger.util.SharedPreferencesManager


class AddShopActivity : AppCompatActivity() {
    lateinit var manageMenuItem: MenuItem
    private lateinit var cacheManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)

        cacheManager = SharedPreferencesManager.getInstance(this)
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
            saveNewName(shopperName.text.toString())
            val returnIntent = Intent()
            returnIntent.putExtra("result", newShop)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            true
        } else super.onOptionsItemSelected(item)

    }

    private fun retrieveMandatoryFields(): List<EditText?> {
        return listOf<EditText?>(expenseName, shopperName, inputTotal)
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

    private fun loadAutoSuggest() {
        cacheManager.putName(value = "Emanuel")
        cacheManager.putName(value = "Damian")
        cacheManager.putName(value = "Neko")
        cacheManager.putName(value = "John")
        cacheManager.putName(value = "Juanin")

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
        /*val arr = listOf<String>( "Emanuel", "Damian", "Neko", "John", "Juan", "Juanin")
        shopperName.threshold = 2
        shopperName.setAdapter(ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arr))*/
        loadAutoSuggest()

        var mandatoryFieldListener = object : TextWatcher {
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
        expenseName.addTextChangedListener(mandatoryFieldListener)
        shopperName.addTextChangedListener(mandatoryFieldListener)
        inputTotal.addTextChangedListener(mandatoryFieldListener)

        val bulletPoint = getString(R.string.bullet_point) + " "
        inputList.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().takeLast(1).equals("\n") and !p0.toString().takeLast(3).equals("\n" + bulletPoint)) {
                    inputList.setText(p0.toString().substring(0, p0.toString().length - 1) + "\n" + bulletPoint)
                    inputList.setSelection(inputList.length())
                }


            }

            override fun beforeTextChanged(textEntered: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(textEntered: CharSequence?, start: Int, end: Int, count: Int) {
                if (!textEntered.isNullOrBlank() and !textEntered?.startsWith(bulletPoint)!! ) {
                    inputList.setText(bulletPoint + textEntered)
                    inputList.setSelection(inputList.length())
                }



            }
        })
    }
}
