package com.example.guceats

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guceats.LoginOrRegister.Login
import com.example.guceats.Shops_Buyer.Shop_AmSaadc
import com.example.guceats.Shops_Buyer.Shop_Arabiata
import com.example.guceats.Shops_Buyer.Shop_LaAroma
import com.example.guceats.Shops_Buyer.Shop_Pronto
import com.example.guceats.Shops_Buyer.Shop_friends
import com.google.firebase.auth.FirebaseAuth

class Map : AppCompatActivity() {

    private lateinit var friend:Button
    private lateinit var Pront:Button
    private lateinit var larom:Button
    private lateinit var amsaadc:Button
    private lateinit var Booster:Button
    private lateinit var simply:Button
    private lateinit var arabiata:Button
    private lateinit var amsaadb:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        friend=findViewById(R.id.friends)
        Pront=findViewById(R.id.Pronto)
        larom=findViewById(R.id.Laroma)
        amsaadc=findViewById(R.id.amsaad_C)
        Booster=findViewById(R.id.Boosters)
        simply=findViewById(R.id.Simply)
        arabiata=findViewById(R.id.Arabiata)
        amsaadb=findViewById(R.id.amsaad_B)

        val buttonClickListener = View.OnClickListener { view ->
            Toast.makeText(applicationContext,findViewById<Button>(view.id).text.toString(),Toast.LENGTH_SHORT).show()
        }
        friend.setOnClickListener(buttonClickListener)
        Pront.setOnClickListener(buttonClickListener)
        larom.setOnClickListener(buttonClickListener)
        amsaadc.setOnClickListener(buttonClickListener)
        Booster.setOnClickListener(buttonClickListener)
        simply.setOnClickListener(buttonClickListener)
        arabiata.setOnClickListener(buttonClickListener)
        amsaadb.setOnClickListener(buttonClickListener)

        val buttonLongClickListener = View.OnLongClickListener { view ->
            when(view.id){
                friend.id->{

                    val i = Intent(this, Shop_friends::class.java)
                    startActivity(i)
                    finish()
                }
                Pront.id->{
                    val i = Intent(this,Shop_Pronto::class.java)
                    startActivity(i)
                    finish()
                }
                larom.id->{
                    val i = Intent(this, Shop_LaAroma::class.java)
                    startActivity(i)
                    finish()
                }
                amsaadc.id->{
                    val i = Intent(this, Shop_AmSaadc::class.java)
                    startActivity(i)
                    finish()
                }
                Booster.id->{
                    val i = Intent(this, shop::class.java)
                    startActivity(i)
                    finish()
                }
                simply.id->{
                    val i = Intent(this, shop::class.java)
                    startActivity(i)
                    finish()
                }
                arabiata.id->{
                    val i = Intent(this, Shop_Arabiata::class.java)
                    startActivity(i)
                    finish()
                }
                amsaadb.id->{
                    val i = Intent(this, shop::class.java)
                    startActivity(i)
                    finish()
                }

            }
            true
        }
        friend.setOnLongClickListener(buttonLongClickListener)
        Pront.setOnLongClickListener(buttonLongClickListener)
        larom.setOnLongClickListener(buttonLongClickListener)
        amsaadc.setOnLongClickListener(buttonLongClickListener)
        Booster.setOnLongClickListener(buttonLongClickListener)
        simply.setOnLongClickListener(buttonLongClickListener)
        arabiata.setOnLongClickListener(buttonLongClickListener)
        amsaadb.setOnLongClickListener(buttonLongClickListener)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menumap,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutbtn -> {
                FirebaseAuth.getInstance().signOut()
                val i = Intent(this, Login::class.java)
                startActivity(i)
                finish()
            }
            R.id.backbtn -> {
                val i = Intent(this, Home::class.java)
                startActivity(i)
                finish()
            }
            R.id.infobtn -> {
                Toast.makeText(applicationContext,"To view vendor's name -> press "+"\n"+ "To Entre a restaurant -> long press",Toast.LENGTH_LONG).show()
            }

        }

        return true
    }
}