package com.example.guceats

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.guceats.LoginOrRegister.Login
import com.example.guceats.Shops_Seller.AmSaadb
import com.example.guceats.Shops_Seller.AmSaadc
import com.example.guceats.Shops_Seller.Arabiata
import com.example.guceats.Shops_Seller.Friends
import com.example.guceats.Shops_Seller.LaAroma
import com.example.guceats.Shops_Seller.Pronto
import com.example.guceats.VoiceOverIP.VoIP
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class Home : AppCompatActivity() {

    private lateinit var map: Button
    private lateinit var update_profile: Button
    private lateinit var profile: TextView
    private var RDb = Firebase.database
    private var userdbref = RDb.getReference("Users")
    private lateinit var toggle: ActionBarDrawerToggle
    private var storage = Firebase.storage
    private var storageRef = storage.reference.child("ProfilePhotos")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val drawerlay = findViewById<DrawerLayout>(R.id.drawer)
        val navbar = findViewById<NavigationView>(R.id.navigation_drawer)
        toggle = ActionBarDrawerToggle(this, drawerlay, R.string.Open, R.string.Close)
        val header = navbar.getHeaderView(0)
        val id = FirebaseAuth.getInstance().currentUser?.uid
        val r = storageRef.child(id.toString())

        val localfile = File.createTempFile("tempimage", "jpg")
        r.getFile(localfile).addOnSuccessListener {
            val b = BitmapFactory.decodeFile(localfile.absolutePath)
            header.findViewById<ImageView>(R.id.imageView3).setImageBitmap(b)
        }

        profile = header.findViewById(R.id.profilename)
        userdbref.child(id.toString()).get().addOnSuccessListener {
            val u = it.getValue(UserModel::class.java)

            profile.text = u?.firstName + " " + u?.lastName
            header.findViewById<TextView>(R.id.profileno).text = u?.number
            if (u?.isVendor() == true)
                header.findViewById<TextView>(R.id.office).text = u?.office
            else
                header.findViewById<TextView>(R.id.office).text = u?.shop
        }
        drawerlay.addDrawerListener(toggle)
        toggle.syncState()

        map = findViewById(R.id.buttonmap)
        update_profile = findViewById(R.id.Update)
        var shop = ""

        map.setOnClickListener { view ->
            userdbref.addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(id.toString())) {
                        userdbref.child(id.toString()).child("shop").get().addOnSuccessListener {
                            shop = it.value as String
                            when (shop) {
                                "Friends" -> {
                                    val i = Intent(this@Home, Friends::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "Pronto" -> {
                                    val i = Intent(this@Home, Pronto::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "L'aroma" -> {
                                    val i = Intent(this@Home, LaAroma::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "3amsaad(c)" -> {
                                    val i = Intent(this@Home, AmSaadc::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "Booster's" -> {
                                    val i =
                                        Intent(this@Home, com.example.guceats.Map::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "Simply" -> {
                                    val i =
                                        Intent(this@Home, com.example.guceats.Map::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "Arabiata" -> {
                                    val i = Intent(this@Home, Arabiata::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                "3amsaad(B)" -> {
                                    val i = Intent(this@Home, AmSaadb::class.java)
                                    startActivity(i)
                                    finish()

                                }

                                else -> {
                                    val i =
                                        Intent(this@Home, com.example.guceats.Map::class.java)
                                    startActivity(i)
                                    finish()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@Home,
                            "please Update your profile with the messing data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Home, "Error in database", Toast.LENGTH_SHORT).show()
                }

            })


        }
        update_profile.setOnClickListener {
            val i = Intent(this, PersonalProfile::class.java)
            startActivity(i)
            finish()
        }

        findViewById<Button>(R.id.buttoncall).setOnClickListener {
            val i = Intent(this, VoIP::class.java)
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
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return true
    }
}