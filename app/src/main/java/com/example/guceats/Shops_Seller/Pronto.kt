package com.example.guceats.Shops_Seller

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.guceats.Home
import com.example.guceats.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Pronto : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private var RDb = Firebase.database
    private var shopdbref = RDb.getReference("Restaurants")


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
                else -> {
                    shopdbref.child(title as String).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild("Data")) {
                                loadFragment(AddItemFrag())
                            }
                            else{
                                Toast.makeText(this@Pronto,"You must complete the shop information to continue",Toast.LENGTH_SHORT).show()
                                val i = Intent(this@Pronto, Shop_data::class.java)
                                i.putExtra("shop",title)
                                startActivity(i)
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@Pronto,"Error in database",Toast.LENGTH_SHORT).show()
                        }

                    })

                    true}
            }
        }



    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopinfomenu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.infoshop -> {
                val i = Intent(this, Shop_data::class.java)
                i.putExtra("shop",title)
                startActivity(i)
                finish()
            }

        }

        return true
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.prontoContainerView,fragment)
        transaction.commit()
    }


}