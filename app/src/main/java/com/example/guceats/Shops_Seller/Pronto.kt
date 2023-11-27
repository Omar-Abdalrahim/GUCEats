package com.example.guceats.Shops_Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.guceats.Home
import com.example.guceats.R
import com.example.guceats.products.Product
import com.google.android.material.bottomnavigation.BottomNavigationView

class Pronto : AppCompatActivity() {
    private var items = ArrayList<Product?>()
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pronto)
        title="Pronto"
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

                    loadFragment(Pronto_Seller())
                    true
                }
                else -> {loadFragment(AddItemFrag())
                    true}
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.prontoContainerView,fragment)
        transaction.commit()
    }}