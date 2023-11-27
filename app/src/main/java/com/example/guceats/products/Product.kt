package com.example.guceats.products

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView

class Product(
    var id: String?="",
    var name: String="",
    var price: Double=0.0,
    var quantity: Int=0,
    var isAvail: Boolean=true,

) {

    override fun toString(): String {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", count='" + quantity + '\'' +
                ", price=" + price +
                ", isAvail=" + isAvail +
                '}'
    }

}