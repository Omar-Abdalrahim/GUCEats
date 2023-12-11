package com.example.guceats.products

class CartItem(
    var product: Product=Product(),
    var count: Int=0,
) {
    override fun toString(): String {
        return "$product Count = $count"
    }
}