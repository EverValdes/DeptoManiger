package com.ever.four.deptomaniger.entity

data class ItemEntity(var name: String, var owner: String, var amount: Int) {
    var currency: String = "$"
    var description: String = ""
    fun clone(): ItemEntity {
        return ItemEntity (name, owner, amount)
    }

}