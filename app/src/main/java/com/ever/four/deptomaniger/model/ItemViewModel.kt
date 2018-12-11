package com.ever.four.deptomaniger.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ever.four.deptomaniger.entity.ItemEntity

class ItemViewModel: ViewModel() {
    private var list =  emptyList<ItemEntity>().toMutableList()
    private var datum: MutableLiveData<MutableList<ItemEntity>> = MutableLiveData()

    init {
        datum.value
    }
    fun getDatumList(): LiveData<MutableList<ItemEntity>> {
        return datum
    }

    fun addDataToList(data: ItemEntity) {
        list.add(data)
        datum.value = list
    }
}