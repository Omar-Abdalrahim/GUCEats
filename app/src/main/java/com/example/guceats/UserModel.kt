package com.example.guceats

import android.text.TextUtils

class UserModel(
    val firstName: String="",
    val lastName: String="",
    val number: String="",
    val office: String="",
    val shop: String="",
    val id: String=""
) {
    fun isVendor(): Boolean {
        if (TextUtils.isEmpty(office))
            return true
        return false
    }

    override fun toString(): String {
        return firstName +"\n"+lastName+"\n"+number+"\n"+office+"\n"+shop+"\n"+id
    }
}
