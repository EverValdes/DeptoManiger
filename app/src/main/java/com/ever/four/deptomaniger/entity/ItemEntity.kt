package com.ever.four.deptomaniger.entity

import java.io.Serializable

data class ItemEntity(var name: String, var shopper: String, var amount: Double): Serializable {
    var currency: String = "$"
    var description: String? = null
    fun clone(): ItemEntity {
        return ItemEntity (name, shopper, amount)
    }

}