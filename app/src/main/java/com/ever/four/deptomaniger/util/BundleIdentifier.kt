package com.ever.four.deptomaniger.util

object BundleIdentifier {
    const val NEW_ITEM: Int = 1
    const val MODIFIED_ITEM: Int = 2
    enum class NEW_SHOP {
        Instance("instance"),
        Name ("name"),
        Description ("description"),
        Shopper ("shopper"),
        Amount ("amount"),
        Index ("index");

        var attribute: String

        constructor(attribute: String) {
            this.attribute = attribute
        }

        override fun toString(): String {
            return this.attribute
        }
    }
}