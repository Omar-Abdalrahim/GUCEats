package com.example.guceats.Shops_Buyer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guceats.Checkout
import com.example.guceats.Home
import com.example.guceats.R
import com.example.guceats.products.CartItem
import com.example.guceats.products.CartItemAdaptor
import com.example.guceats.products.ItemsAdaptor
import com.example.guceats.products.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class CartFragment : Fragment() {
    private var cartitems = ArrayList<CartItem?>()
    private var RDb = Firebase.database
    private var cartdbref = RDb.getReference("Carts")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_cart, container, false)

        val id = Firebase.auth.currentUser?.uid
        cartdbref.child(id.toString()).child("Items").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (sn in snapshot.children) {
                    val p = sn.getValue(CartItem::class.java)
                    println(p)
                    cartitems.add(p)
                }

                val adapter = CartItemAdaptor(cartitems)
                val rvprod = v.findViewById<View>(R.id.carrec) as RecyclerView
                rvprod.adapter = adapter
                println(cartitems)
                println(cartitems.size)
                println(adapter.itemCount)
                 cartdbref.child(id.toString()).child("checkout_price").get().addOnSuccessListener {
                     v.findViewById<TextView>(R.id.textView7).text= it.value.toString()+ " LE"
                }
                rvprod.layoutManager = GridLayoutManager(v.context, 1, GridLayoutManager.VERTICAL, false)


                cartitems = ArrayList<CartItem?>()
                v.findViewById<Button>(R.id.buttoncheck).setOnClickListener {
                    val i = Intent(v.context, Checkout::class.java)
                    i.putExtra("Shop_name",getShop())
                    startActivity(i)

                }
                println(getShop())
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

        return v
    }
   private fun getShop(): String {

       if (context.toString().contains("friends")) return "Friends"
       if (context.toString().contains("Pronto")) return "Pronto"
       if (context.toString().contains("ِAmSaadc")) return "3amsaad(c)"
       if (context.toString().contains("Laaroma")) return "L'aroma"
       if (context.toString().contains("Arabiata")) return "Arabiata"
       if (context.toString().contains("ِAmSaadb")) return "3amsaad(b)"

       return ""
   }

}