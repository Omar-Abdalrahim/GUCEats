package com.example.guceats.Shops_Seller

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.guceats.R
import com.example.guceats.products.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class EditProduct : AppCompatActivity() {
    val REQUEST_IMAGE_GET = 1
    val REQUEST_IMAGE_CAPTURE = 2
    private var fullPhotoUri: Uri? = null
    private var RDb = Firebase.database
    private var storage = Firebase.storage
    private var userdbref = RDb.getReference("Users")
    private var shopdbref = RDb.getReference("Restaurants")
    private var storageRef = storage.reference.child("Restaurants")
    private lateinit var s :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        title = "Edit"
        var name = findViewById<EditText>(R.id.editname).text
        var price = findViewById<EditText>(R.id.editprice).text
        var quantity = findViewById<EditText>(R.id.editquantity).text
        var image = findViewById<ImageView>(R.id.Editimageview)
        var Edit = findViewById<Button>(R.id.Done)
        var addimagebtn = findViewById<Button>(R.id.editaddimage)
        addimagebtn.setOnClickListener {
            openDialog()
        }
        Edit.setOnClickListener {
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity) || image.drawable == null) {
                Toast.makeText(this, "Please fill the required fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val oldp=Product(
                    intent.getStringExtra("productid"),
                    intent.getStringExtra("productname").toString(),
                    intent.getDoubleExtra("productprice",0.0),
                    intent.getIntExtra("productquantity",0),
                    intent.getBooleanExtra("productavailable",true)

                )
                val p = Product(
                    intent.getStringExtra("productid"),
                    name.toString(),
                    price.toString().toDouble(),
                    quantity.toString().toInt(),
                    true,
                )
                val id = FirebaseAuth.getInstance().currentUser?.uid
                userdbref.child(id.toString()).child("shop").get().addOnSuccessListener {
                    s = it.value as String
                    val n = name.toString()
                    var r = storageRef.child(s).child(p.name)
                    println(r)
                    r.putFile(fullPhotoUri!!)
                    shopdbref.child(s).child("Menu").child(n).setValue(p)

                }.addOnCompleteListener {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    remove(oldp)

                }
            }
        }
    }

    private fun leave() {
        when(s){
            "Friends"-> {
                val i = Intent(this, Friends_Seller::class.java)
                startActivity(i)
                finish()

            }
            "Pronto"->{
                val i = Intent(this, com.example.guceats.Map::class.java)
                startActivity(i)
                finish()

            }
            "L'aroma"-> {
                val i = Intent(this, com.example.guceats.Map::class.java)
                startActivity(i)
                finish()

            }
            "3amsaad(c)"->{
                val i = Intent(this, com.example.guceats.Map::class.java)
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
                val i = Intent(this, com.example.guceats.Map::class.java)
                startActivity(i)
                finish()

            }
            "3amsaad(B)"->{
                val i = Intent(this, com.example.guceats.Map::class.java)
                startActivity(i)
                finish()

            }
        }
        println("done")
    }

    private fun remove(p:Product){
        shopdbref.child(s).child("Menu").child(p.name).removeValue()
        storageRef.child(s).child(p.name).delete()
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
                dispatchTakePictureIntent()
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

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // Display error state to the user.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            fullPhotoUri = data?.data
            findViewById<ImageView>(R.id.Editimageview).setImageURI(fullPhotoUri)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            fullPhotoUri = data?.data
            findViewById<ImageView>(R.id.Editimageview).setImageBitmap(thumbnail)
        }
    }
}
