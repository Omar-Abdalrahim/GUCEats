package com.example.guceats

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.guceats.LoginOrRegister.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class PersonalProfile : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var save: Button
    private lateinit var fname: EditText
    private lateinit var lname: EditText
    private lateinit var phone: EditText
    private lateinit var office: EditText
    private lateinit var officetxt: TextView
    private lateinit var r1: RadioGroup
    private lateinit var sp: Spinner
    private lateinit var role: String
    private lateinit var shop: String
    private val list: ArrayList<String> =
        arrayListOf(
            "Friends", "Pronto",
            "L'aroma",
            "3amsaad(c)",
            "Booster's",
            "Simply",
            "Arabiata",
            "3amsaad(B)"
        )
    private var RDb = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_profile)
        save = findViewById(R.id.Savebtn)
        fname = findViewById(R.id.editTextTextPersonName)
        lname = findViewById(R.id.editTextTextPersonName2)
        office = findViewById(R.id.editTextTextPersonName3)
        officetxt = findViewById(R.id.textView5)
        phone = findViewById(R.id.editTextPhone)
        r1 = findViewById(R.id.radioGroup)
        sp = findViewById(R.id.spinner)

        val adp1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adp1

        r1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("User")) {
                sp.visibility = View.INVISIBLE
                officetxt.visibility = View.VISIBLE
                office.visibility = View.VISIBLE
            } else {
                officetxt.visibility = View.INVISIBLE
                office.visibility = View.INVISIBLE
                sp.visibility = View.VISIBLE
            }
            role = radio.text.toString()
        }
        sp.onItemSelectedListener = this


        save.setOnClickListener { view ->

            if (!(TextUtils.isEmpty(fname.text.toString()) || TextUtils.isEmpty(lname.text.toString())
                        || TextUtils.isEmpty(phone.text.toString())
                        || role == null)
            ) {

                val cur = Firebase.auth.currentUser
                lateinit var u: UserModel
                if (role.equals("User")) {
                    if (!TextUtils.isEmpty(office.text.toString())) {
                        //create User
                        u = UserModel(
                            fname.text.toString(),
                            lname.text.toString(),
                            phone.text.toString(),
                            office.text.toString(),
                            "",
                            cur?.uid.toString()
                        )
                        val userdbref = RDb.getReference("Users")

                        userdbref.child(u.id).setValue(u)
                        save.visibility = View.INVISIBLE
                        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "please fill the office field", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    if (shop != null) {
                        //create vendor
                        u = UserModel(
                            fname.text.toString(),
                            lname.text.toString(),
                            phone.text.toString(),
                            "", shop,
                            cur?.uid.toString()
                        )
                        val userdbref = RDb.getReference("Users")

                        userdbref.child(u.id).setValue(u)
                        save.visibility = View.INVISIBLE
                        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "please choose a shop", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            } else
                Toast.makeText(applicationContext, "please fill all fields", Toast.LENGTH_SHORT)
                    .show()

        }
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
        shop = list[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menumap, menu)
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
            R.id.backbtn -> {
                if ((TextUtils.isEmpty(fname.text.toString()) || TextUtils.isEmpty(lname.text.toString())
                            || TextUtils.isEmpty(phone.text.toString())
                            || role == null)
                )
                    Toast.makeText(this,"please fill all fields",Toast.LENGTH_SHORT).show()
                else {
                    val i = Intent(this, Home::class.java)
                    i.putExtra("shop_name", shop)
                    startActivity(i)
                    finish()
                }
            }

        }

        return true
    }

}