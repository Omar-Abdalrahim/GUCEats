package com.example.guceats.Shops_Buyer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guceats.R
import com.example.guceats.products.ItemsAdaptor
import com.example.guceats.products.Product
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

private var items = ArrayList<Product?>()
private var RDb = Firebase.database
private var userdbref = RDb.getReference("Users")
private var f=false
private var shopdbref = RDb.getReference("Restaurants")
class Shop_Pronto_frag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getMenu()

        val view=inflater.inflate(R.layout.fragment_shop__pronto_frag, container, false)
        val adapter = ItemsAdaptor(items)
        val rvprod = view.findViewById<View>(R.id.prontobrec) as RecyclerView
        rvprod.adapter = adapter
        println(items)
        println(items.size)
        println(adapter.itemCount)
        rvprod.layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)

        items = ArrayList<Product?>()
        //sendToDatabase()
        return view
    }

    private fun getMenu(){
        shopdbref.child("Pronto").child("Menu").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (sn in snapshot.children) {
                    val p = sn.getValue(Product::class.java)
                    items.add(p);
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    private fun sendToDatabase() {
        var r = shopdbref.child("Pronto").child("Menu")
        items.forEach { product -> r.child(product?.name.toString()).setValue(product)  }
    }
}