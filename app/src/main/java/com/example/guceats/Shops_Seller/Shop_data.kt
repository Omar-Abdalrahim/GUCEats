package com.example.guceats.Shops_Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guceats.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Shop_data : AppCompatActivity() {
    private var RDb = Firebase.database
    private var shopdbref = RDb.getReference("Restaurants")
    private lateinit var num: EditText
    private lateinit var fee: EditText
    private lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_data)
        title=intent.getStringExtra("shop").toString()+ " Info"

        num = findViewById(R.id.editTextnumber)
        fee = findViewById(R.id.editTextfee)
        btn = findViewById(R.id.buttoncont)

        shopdbref.child(intent.getStringExtra("shop").toString())
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("Data")) {
                        shopdbref.child(intent.getStringExtra("shop").toString()).child("Data")
                            .child("number").get().addOnSuccessListener {
                                num.setText(it.value as String)
                                println(num.text)
                            }
                        shopdbref.child(intent.getStringExtra("shop").toString()).child("Data")
                            .child("Delivery_fee").get().addOnSuccessListener {
                                fee.setText(it.value as String)
                            }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Shop_data, "Error in database", Toast.LENGTH_SHORT).show()
                }

            })
        btn.setOnClickListener {
            if ((TextUtils.isEmpty(num.text.toString()) || TextUtils.isEmpty(fee.text.toString())))
                Toast.makeText(this, "Please Fill all the Required Fields", Toast.LENGTH_SHORT)
                    .show()
            else {
                shopdbref.child(intent.getStringExtra("shop").toString()).child("Data")
                    .child("number").setValue(num.text.toString())
                shopdbref.child(intent.getStringExtra("shop").toString()).child("Data")
                    .child("Delivery_fee").setValue(fee.text.toString())

                getintent()
            }
        }

    }
        private fun getintent(){
            when(intent.getStringExtra("shop")){
                "Friends" -> { val i = Intent(this, Friends::class.java)
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
                    val i = Intent(this, com.example.guceats.Map::class.java)
                    startActivity(i)
                    finish()

                }

                "Simply" -> {
                    val i = Intent(this, com.example.guceats.Map::class.java)
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
}
