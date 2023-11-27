package com.example.guceats.products

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guceats.R
import com.example.guceats.Shops_Seller.EditProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


//import com.squareup.picasso.Picasso
private var RDb = Firebase.database
private var storage = Firebase.storage
private var userdbref = RDb.getReference("Users")
private var cartdbref = RDb.getReference("Carts")
private var storageRef = storage.reference

class ItemsAdaptor(private val mitems: ArrayList<Product?>) :
    RecyclerView.Adapter<ItemsAdaptor.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row

        val itemImage = itemView.findViewById<ImageView>(R.id.imageView)
        val textView1 = itemView.findViewById<TextView>(R.id.itemname)
        val textView2 = itemView.findViewById<TextView>(R.id.price)
        val textView3 = itemView.findViewById<TextView>(R.id.availability)
        val ImageButton = itemView.findViewById<ImageButton>(R.id.imageButton)
        val editbtn = itemView.findViewById<ImageButton>(R.id.imageButtonedit)

    }

    private lateinit var par: ViewGroup

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdaptor.ViewHolder {
        par = parent
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val prodView = inflater.inflate(R.layout.product, parent, false)
        // Return a new holder instance

        return ViewHolder(prodView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ItemsAdaptor.ViewHolder, position: Int) {
        // Get the data model based on position
        val p: Product? = mitems.get(position)

        val nametextView = viewHolder.textView1
        nametextView.text = p?.name

        val pricetextView = viewHolder.textView2
        pricetextView.text = p?.price.toString() + " LE"

        val aviltextView = viewHolder.textView3

        if (p?.isAvail == true && p.quantity != 0) {
            aviltextView.text = "Available"
            aviltextView.setTextColor(Color.parseColor("#50C878"))

        } else {
            aviltextView.text = "UnAvailable"
            aviltextView.setTextColor(Color.parseColor("#D2042D"))
        }
        val id = FirebaseAuth.getInstance().currentUser?.uid
        val ebtn = viewHolder.editbtn
        val btn=viewHolder.ImageButton

        userdbref.child(id.toString()).child("shop").get().addOnSuccessListener {
            val s: String
            if (!TextUtils.isEmpty(it.value.toString())) {
                s = it.value as String
                btn.visibility=View.GONE
            }
            else {
                s = getShop()
                ebtn.visibility=View.GONE
            }

            val itemImage = viewHolder.itemImage


//            storageRef.child("Restaurants/"+s+"/"+nametextView.text.toString()+"/jpeg").downloadUrl.addOnCompleteListener {
//                itemImage.setImageURI(it.result)
//
//            }
            println("S is "+s)
            val r = storageRef.child("Restaurants").child(s).child(nametextView.text.toString())
            val localfile = File.createTempFile("tempimage", "jpg")
            r.getFile(localfile).addOnSuccessListener {
                val b = BitmapFactory.decodeFile(localfile.absolutePath)
                itemImage.setImageBitmap(b)
            }
            btn.setOnClickListener {
                val citem=CartItem(p!!,1)
                cartdbref.child(id.toString()).child("Items").child(nametextView.text.toString()).setValue(citem)
            }

            ebtn.setOnClickListener {
                val intent = Intent(par.context, EditProduct::class.java)
                intent.putExtra("productid", p?.id)
                intent.putExtra("productname", p?.name)
                intent.putExtra("productprice", p?.price)
                intent.putExtra("productquantity", p?.quantity)
                intent.putExtra("productavailable", p?.isAvail)
                par.context.startActivity(intent)

            }



        }

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mitems.size
    }

    private fun getShop(): String {
        println("-----------------")
        println(par.context.toString())
        if (par.context.toString().contains("friends")) return "Friends"
        if (par.context.toString().contains("Pronto")) return "Pronto"
        if (par.context.toString().contains("ِAmSaadc")) return "3amsaad(c)"
        if (par.context.toString().contains("Laaroma")) return "L'aroma"
        if (par.context.toString().contains("Arabiata")) return "Arabiata"
        if (par.context.toString().contains("ِAmSaadb")) return "3amsaad(b)"

        return ""
    }




}
