package com.example.guceats.Shops_Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.guceats.Home
import com.example.guceats.R
import com.example.guceats.products.Product
import com.google.android.material.bottomnavigation.BottomNavigationView

class LaAroma : AppCompatActivity() {
    private var items = ArrayList<Product?>()
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_la_aroma)
        title="L'Aroma"
        bottomNav = findViewById(R.id.bottomNavSeller)
        bottomNav.menu.findItem(R.id.menuseller).isChecked = true;
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeseller -> {
                    val i = Intent(this, Home::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.menuseller -> {

                    loadFragment(LaAroma_Seller())
                    true
                }
                else -> {loadFragment(AddItemFrag())
                    true}
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.laaromaContainerView,fragment)
        transaction.commit()
    }
}