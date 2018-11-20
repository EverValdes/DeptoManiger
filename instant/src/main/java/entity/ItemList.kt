package entity

data class ItemList (var name: String, var owner: String, var description: String, var amount: Int) {
    var currency: String = "$"
    fun clone(): ItemList {
        return ItemList (name, owner, description, amount)
    }

}