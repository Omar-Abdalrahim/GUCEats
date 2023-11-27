package com.example.guceats.Shops_Buyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guceats.Home
import com.example.guceats.R
import com.example.guceats.Shops_Seller.AddItemFrag
import com.example.guceats.Shops_Seller.Friends_Seller
import com.google.android.material.bottomnavigation.BottomNavigationView

class Shop_friends : AppCompatActivity() {
    private lateinit var t: TextView
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_friends)
        title="Friends"
        bottomNav = findViewById(R.id.bottomNavBuyer)
        bottomNav.menu.findItem(R.id.menubuyer).isChecked = true;
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.cart -> {
                    loadFragment(CartFragment())
                    true
                }
                R.id.menubuyer -> {

                    loadFragment(Shop_friends_Frag())
                    true
                }
                else -> {
                    val i = Intent(this, Home::class.java)
                    startActivity(i)
                    finish()
                    true}
            }
        }



    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.friendsContainerViewbuyer,fragment)
        transaction.commit()
    }



}