package com.example.guceats

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.guceats.Shops_Seller.AmSaadb
import com.example.guceats.Shops_Seller.AmSaadc
import com.example.guceats.Shops_Seller.Arabiata
import com.example.guceats.Shops_Seller.Friends
import com.example.guceats.Shops_Seller.LaAroma
import com.example.guceats.Shops_Seller.Pronto
import com.example.guceats.products.CartItem
import com.example.guceats.products.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class Checkout : AppCompatActivity() {
    private var RDb = Firebase.database
    private var storage = Firebase.storage
    private var userdbref = RDb.getReference("Users")
    private var shopdbref = RDb.getReference("Restaurants")
    private var storageRef = storage.reference.child("Restaurants")
    private var cartdbref = RDb.getReference("Carts")
    private var requests = RDb.getReference("Requests")
    private val id = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var sub: TextView
    private lateinit var fee: TextView
    private lateinit var tot: TextView
    private lateinit var order: Button
    private var CHANNEL_ID = "Order"
    private var total: Double = 0.0
    private val context = this

    private lateinit var notificationman: NotificationManagerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        title = "Checkout"

        sub = findViewById(R.id.Subtotal)
        fee = findViewById(R.id.dfee)
        tot = findViewById(R.id.total)
        order = findViewById(R.id.buttoncont)

        cartdbref.child(id.toString()).child("checkout_price").get().addOnSuccessListener {
            sub.text = it.value.toString() + " LE"
        }
        shopdbref.child(intent.getStringExtra("Shop_name").toString()).child("Data")
            .child("Delivery_fee").get().addOnSuccessListener {
                fee.text = it.value.toString() + " LE"
                total = sub.text.subSequence(0, sub.text.length - 3).toString()
                    .toDouble() + it.value.toString().toDouble()
                tot.text = total.toString() + " LE"
            }


        order.setOnClickListener {
            userdbref.child(id.toString()).get().addOnSuccessListener {
                val u = it.getValue(UserModel::class.java)
                cartdbref.child(id.toString()).child("Items").get().addOnSuccessListener {
                    val cart = ArrayList<CartItem>()
                    for (i in it.children)
                        cart.add(i.getValue(CartItem::class.java)!!)
                    val r = Request(u!!, cart)
                    shopdbref.child(intent.getStringExtra("Shop_name").toString()).child("Requests")
                        .child(u?.firstName + " " + u?.lastName).setValue(r)
                    for (i in cart){
                        i.product.quantity-=i.count
                        shopdbref.child(intent.getStringExtra("Shop_name").toString()).child("Menu").child(i.product.name).setValue(i.product)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        createNotificationChannel()
                        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(intent.getStringExtra("Shop_name").toString())
                            .setContentText("Thank You for Ordering ♥!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
                        notificationman = NotificationManagerCompat.from(this)

                        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissions(
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                            )
                            notificationman.notify(0, builder)

                        }
                    }
                    cartdbref.child(id.toString()).removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Order Placed", Toast.LENGTH_LONG).show()
                        getintent()

                    }
                }
            }
        }


    }

    private fun getintent() {

        when (intent.getStringExtra("Shop_name").toString()) {
            "Friends" -> {
                val i = Intent(this, Friends::class.java)
                startActivity(i)
                finish()
            }

            "Pronto" -> {
                val i = Intent(this, Pronto::class.java)
                startActivity(i)
                finish()

            }

            "L'aroma" -> {
                val i = Intent(this, LaAroma::class.java)
                startActivity(i)
                finish()

            }

            "3amsaad(c)" -> {
                val i = Intent(this, AmSaadc::class.java)
                startActivity(i)
                finish()

            }

            "Booster's" -> {
                val i = Intent(this, Map::class.java)
                startActivity(i)
                finish()

            }

            "Simply" -> {
                val i = Intent(this, Map::class.java)
                startActivity(i)
                finish()

            }

            "Arabiata" -> {
                val i = Intent(this, Arabiata::class.java)
                startActivity(i)
                finish()

            }

            "3amsaad(B)" -> {
                val i = Intent(this, AmSaadb::class.java)
                startActivity(i)
                finish()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val desc = "A Description of the Channel"
        val channel =
            NotificationChannel(CHANNEL_ID, "Notif Channel", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}