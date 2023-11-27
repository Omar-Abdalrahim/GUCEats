package com.example.guceats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guceats.LoginOrRegister.Login
import com.example.guceats.Shops_Seller.AmSaadb
import com.example.guceats.Shops_Seller.AmSaadc
import com.example.guceats.Shops_Seller.Arabiata
import com.example.guceats.Shops_Seller.Friends
import com.example.guceats.Shops_Seller.LaAroma
import com.example.guceats.Shops_Seller.Pronto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private lateinit var map: Button
    private lateinit var update_profile: Button
    private var RDb = Firebase.database
    private var userdbref = RDb.getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        map = findViewById(R.id.buttonmap)
        update_profile = findViewById(R.id.Update)
        var shop =""
        map.setOnClickListener { view ->
            val id= FirebaseAuth.getInstance().currentUser?.uid
            userdbref.child(id.toString()).child("shop").get().addOnSuccessListener {
                shop= it.value as String
                when(shop){
                    "Friends"-> {
                        val i = Intent(this, Friends::class.java)
                        startActivity(i)
                        finish()

                    }
                    "Pronto"->{
                        val i = Intent(this, Pronto::class.java)
                        startActivity(i)
                        finish()

                    }
                    "L'aroma"-> {
                        val i = Intent(this, LaAroma::class.java)
                        startActivity(i)
                        finish()

                    }
                    "3amsaad(c)"->{
                        val i = Intent(this, AmSaadc::class.java)
                        startActivity(i)
                        finish()

                    }
                    "Booster's"-> {
                        val i = Intent(this, com.example.guceats.Map::class.java)
                        startActivity(i)
                        finish()

                    }
                    "Simply"->{
                        val i = Intent(this, com.example.guceats.Map::class.java)
                        startActivity(i)
                        finish()

                    }
                    "Arabiata"-> {
                        val i = Intent(this, Arabiata::class.java)
                        startActivity(i)
                        finish()

                    }
                    "3amsaad(B)"->{
                        val i = Intent(this, AmSaadb::class.java)
                        startActivity(i)
                        finish()

                    }
                    else ->{
                        val i = Intent(this, com.example.guceats.Map::class.java)
                        startActivity(i)
                        finish()
                    }
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }


        }
        update_profile.setOnClickListener { view ->
            val i = Intent(this, PersonalProfile::class.java)
            startActivity(i)
            finish()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutbtn -> {
                FirebaseAuth.getInstance().signOut()
                val i = Intent(this, Login::class.java)
                startActivity(i)
                finish()
            }

        }

        return true
    }
}