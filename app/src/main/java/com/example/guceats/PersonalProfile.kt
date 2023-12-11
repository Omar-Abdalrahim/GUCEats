package com.example.guceats

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.guceats.LoginOrRegister.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
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
    val REQUEST_IMAGE_GET = 1
    val REQUEST_IMAGE_CAPTURE = 2
    private var fullPhotoUri: Uri? = null
    private var thumbnail: Bitmap? = null
    private var storage = Firebase.storage
    private lateinit var addphoto: Button
    private var userdbref = RDb.getReference("Users")
    private var storageRef = storage.reference.child("ProfilePhotos")
    private var uploaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_profile)
        println(findViewById<ImageView>(R.id.profileimageview).drawable == null)
        save = findViewById(R.id.Savebtn)
        fname = findViewById(R.id.editTextTextPersonName)
        lname = findViewById(R.id.editTextTextPersonName2)
        office = findViewById(R.id.editTextTextPersonName3)
        officetxt = findViewById(R.id.textView5)
        phone = findViewById(R.id.editTextPhone)
        r1 = findViewById(R.id.radioGroup)
        sp = findViewById(R.id.spinner)
        addphoto = findViewById(R.id.buttonperson)

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

        addphoto.setOnClickListener {
            openDialog()
        }
        val id = FirebaseAuth.getInstance().currentUser?.uid
        userdbref.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(id.toString())) {
                    userdbref.child(id.toString()).get().addOnSuccessListener {
                        val u = it.getValue(UserModel::class.java)
                        fname.setText(u?.firstName)
                        lname.setText(u?.lastName)
                        phone.setText(u?.number)
                        if (u?.isVendor()!!) {
                            r1.check(R.id.radioButton7)
                            officetxt.visibility = View.INVISIBLE
                            office.visibility = View.INVISIBLE
                            sp.visibility = View.VISIBLE
                            sp.setSelection(list.indexOf(u.shop))

                        } else {
                            r1.check(R.id.radioButton6)
                            sp.visibility = View.INVISIBLE
                            officetxt.visibility = View.VISIBLE
                            office.visibility = View.VISIBLE
                            office.setText(u.office)
                        }
                        val localfile = File.createTempFile("tempimage", "jpg")
                        storageRef.child(id.toString()).getFile(localfile).addOnSuccessListener {
                            val b = BitmapFactory.decodeFile(localfile.absolutePath)
                            findViewById<ImageView>(R.id.profileimageview).setImageBitmap(b)
                            uploaded = true
                        }

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PersonalProfile, "Error in database", Toast.LENGTH_SHORT).show()
            }

        })

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
                        userdbref = RDb.getReference("Users")

                        userdbref.child(u.id).setValue(u)
                        save.visibility = View.INVISIBLE
                        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "please fill the office field",
                            Toast.LENGTH_SHORT
                        )
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
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "please choose a shop",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }


            } else
                Toast.makeText(applicationContext, "please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            if (!uploaded) {
                val id = FirebaseAuth.getInstance().currentUser?.uid
                var r = storageRef.child(id.toString())
                //println(r)

                r.putFile(fullPhotoUri!!)
                uploaded = true
            }

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
            R.id.infobtn->{
                Toast.makeText(this,"Fill the missing information",Toast.LENGTH_SHORT).show()
            }

            R.id.backbtn -> {
                if ((TextUtils.isEmpty(fname.text.toString()) || TextUtils.isEmpty(lname.text.toString())
                            || TextUtils.isEmpty(phone.text.toString())
                            || role == null)
                )
                    Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show()
                else {
                    if (!uploaded)
                        Toast.makeText(this, "please upload a profile pic", Toast.LENGTH_SHORT)
                    else {
                        val i = Intent(this, Home::class.java)
                        i.putExtra("shop_name", shop)
                        i.putExtra("flag", true)
                        startActivity(i)
                        finish()
                    }
                }
            }

        }

        return true
    }

    private fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Choose")
            .setTitle("Upload Image")
            .setPositiveButton("Gallery") { dialog, which ->
                selectImage()

            }
            .setNegativeButton("Take a Photo") { dialog, which ->
                dispatchTakePictureIntent("photo")
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


    fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_GET)
    }

    private fun dispatchTakePictureIntent(targetFilename: String) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {

            putExtra(
                MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(fullPhotoUri, targetFilename)
            )
        }
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // Display error state to the user.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            fullPhotoUri = data?.data
            findViewById<ImageView>(R.id.profileimageview).setImageURI(fullPhotoUri)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            thumbnail = data?.getParcelableExtra("data")
            //fullPhotoUri = data.extras
            findViewById<ImageView>(R.id.profileimageview).setImageURI(fullPhotoUri)
        }
    }

}