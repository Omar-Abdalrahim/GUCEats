package com.example.guceats

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class shop : AppCompatActivity() {
    private var RDb = Firebase.database
    private var userdbref = RDb.getReference("Users")
    private var menudbref = RDb.getReference("Menus")
    private lateinit var CurrUserModel: UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        /*var id = FirebaseAuth.getInstance().currentUser?.uid
        userdbref.child(id.toString()).get().addOnSuccessListener {
            CurrUserModel= it.getValue(UserModel::class.java)!!
                if (CurrUserModel.isVendor()) {       //vendor
                    val s = CurrUserModel.shop
                    this.title = s
                    println("+++++++++++++++++")
                    println(CurrUserModel)
                   // cont_vendor()

                } else { // user
                   // cont_buyer()
                }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }*/


    }

    private fun cont_vendor() {
        TODO("Not yet implemented")
    }

    private fun cont_buyer() {
        TODO("Not yet implemented")
    }


}