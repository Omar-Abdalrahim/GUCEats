package com.example.guceats.Shops_Seller

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guceats.R
import com.example.guceats.products.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID


class AddItemFrag : Fragment() {
    val REQUEST_IMAGE_GET = 1
    val REQUEST_IMAGE_CAPTURE = 2
    private var fullPhotoUri: Uri? = null
    private var thumbnail: Bitmap? = null
    private var RDb = Firebase.database
    private var storage = Firebase.storage
    private var userdbref = RDb.getReference("Users")
    private var shopdbref = RDb.getReference("Restaurants")
    private var storageRef = storage.reference.child("Restaurants")

    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_item, container, false)
        view.findViewById<Button>(R.id.addimage).setOnClickListener { v ->
            openDialog()
        }

        view.findViewById<Button>(R.id.addtoitems).setOnClickListener { v ->
            val name = view.findViewById<EditText>(R.id.editTextname).text
            val price = view.findViewById<EditText>(R.id.editTextprice).text
            val quantity = view.findViewById<EditText>(R.id.editTextcount).text
            val image = view.findViewById<ImageView>(R.id.imageView2)
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(quantity) || image.drawable == null) {
                Toast.makeText(view.context, "Please fill the required fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val p = Product(
                    createID(),
                    name.toString(),
                    price.toString().toDouble(),
                    quantity.toString().toInt(),
                    true,

                    )
                val id = FirebaseAuth.getInstance().currentUser?.uid
                userdbref.child(id.toString()).child("shop").get().addOnSuccessListener {
                    val s = it.value as String
                    val n = name.toString()
                    var r = storageRef.child(s).child(n)
                    println(r)
                    r.putFile(fullPhotoUri!!)
                    shopdbref.child(s).child("Menu").child(name.toString()).setValue(p)

                }.addOnCompleteListener {
                    Toast.makeText(view.context, "Success", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }

    @Throws(Exception::class)
    fun createID(): String? {
        return UUID.randomUUID().toString().replace("-", "").uppercase()
    }

    private fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
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
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            fullPhotoUri = data?.data
            view.findViewById<ImageView>(R.id.imageView2).setImageURI(fullPhotoUri)
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            thumbnail = data?.getParcelableExtra("data")
            //fullPhotoUri = data.extras
            view.findViewById<ImageView>(R.id.imageView2).setImageURI(fullPhotoUri)
        }
    }

}