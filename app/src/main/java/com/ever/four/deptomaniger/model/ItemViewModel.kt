package com.ever.four.deptomaniger.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ever.four.deptomaniger.entity.ItemEntity
import com.google.firebase.database.FirebaseDatabase

class ItemViewModel: ViewModel() {
    private var list =  emptyList<ItemEntity>().toMutableList()
    private var datum: MutableLiveData<MutableList<ItemEntity>> = MutableLiveData()

    init {
        datum.value = list
        var dbRef = FirebaseDatabase.getInstance().reference
    }
    fun getDatumList(): LiveData<MutableList<ItemEntity>> {
        return datum
    }

    fun setDataToList(index: Int, data: ItemEntity) {
        list[index] = data
        datum.value = list
    }

    fun setDataToList(data: ItemEntity) {
        list.add(data)
        datum.value = list
    }
}