package com.example.guceats.products


import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.guceats.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


//import com.squareup.picasso.Picasso
private var RDb = Firebase.database
private var storage = Firebase.storage
private var cartdbref = RDb.getReference("Carts")
private var storageRef = storage.reference


class CartItemAdaptor(private val mitems: ArrayList<CartItem?>) :
    RecyclerView.Adapter<CartItemAdaptor.ViewHolder>() {
    // Your holder should contain and initialize a member variable
    private lateinit var par: ViewGroup

    private var s = ""
    private var checkout_price = 0.0

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // for any view that will be set as you render a row
        val itemImage = itemView.findViewById<ImageView>(R.id.imageView_cartitem)
        val textView1 = itemView.findViewById<TextView>(R.id.itemname_cartitem)
        val textView2 = itemView.findViewById<TextView>(R.id.price_cartitem)
        val textView3 = itemView.findViewById<TextView>(R.id.availability_cartitem)
        val textViewtotalprice = itemView.findViewById<TextView>(R.id.textView10)
        val textviewcount = itemView.findViewById<TextView>(R.id.itemcount)
        val ImageButton = itemView.findViewById<ImageButton>(R.id.imageButton_cartitem)

        val ImageButton2 = itemView.findViewById<ImageButton>(R.id.imageButtonremove_cartitem)
        // val checkoutprice=itemView.findViewById<TextView>(R.id.textView7)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemAdaptor.ViewHolder {
        par = parent
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val prodView = inflater.inflate(R.layout.cart_item, parent, false)
        // Return a new holder instance

        return ViewHolder(prodView)
    }


    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: CartItemAdaptor.ViewHolder, position: Int) {
        val citem: CartItem? = mitems.get(position)

        val image = viewHolder.itemImage
        val textviewname = viewHolder.textView1
        val textviewprice = viewHolder.textView2
        val textviewavail = viewHolder.textView3
        val textviewTprice = viewHolder.textViewtotalprice
        val textViewcount = viewHolder.textviewcount
        val addbtn = viewHolder.ImageButton
        val removebtn = viewHolder.ImageButton2
        //val checkouttextview=viewHolder.checkoutprice

        textViewcount.text = citem?.count.toString()
        if (textViewcount.text.toString() != "0") {

            textviewname.text = citem?.product?.name
            textviewprice.text = citem?.product?.price.toString() + " LE"
            if (citem?.product?.isAvail == true && citem.product.quantity != 0) {
                textviewavail.text = "Available"
                textviewavail.setTextColor(Color.parseColor("#50C878"))

            } else {
                textviewavail.text = "UnAvailable"
                textviewavail.setTextColor(Color.parseColor("#D2042D"))
            }
            textviewTprice.text =
                ((citem?.product?.price.toString().toDouble()) * textViewcount.text.toString()
                    .toInt()).toString() + " LE"
            checkout_price += textviewTprice.text.subSequence(0, textviewprice.text.length - 3)
                .toString().toDouble()

            s = getShop()
            val r = storageRef.child("Restaurants").child(s).child(textviewname.text.toString())
            val localfile = File.createTempFile("tmp", "jpg")
            r.getFile(localfile).addOnSuccessListener {
                val b = BitmapFactory.decodeFile(localfile.absolutePath)
                image.setImageBitmap(b)
            }
            val id = Firebase.auth.currentUser?.uid.toString()
            addbtn.setOnClickListener {
                if (textViewcount.text.toString().toInt() + 1 >= citem!!.product.quantity)
                    Toast.makeText(par.context, "Not Enough Items in Stock", Toast.LENGTH_SHORT)
                        .show()
                else {
                    citem.count = citem.count.toString().toInt() + 1

                    textViewcount.text = citem.count.toString()
                    textviewTprice.text =
                        ((citem.product.price.toString()
                            .toDouble()) * textViewcount.text.toString()
                            .toInt()).toString() + " LE"
                    checkout_price += textviewprice.text.subSequence(
                        0,
                        textviewTprice.text.length - 3
                    ).toString().toDouble()


                }
                cartdbref.child(id).child("Items").child(textviewname.text.toString()).setValue(citem)
                println(checkout_price)
                cartdbref.child(id).child("checkout_price").setValue(checkout_price)
            }


            removebtn.setOnClickListener {
                if (textViewcount.text.toString().toInt() - 1 == 0) {
                    println("deleted")
                    mitems.remove(citem)
                    println(cartdbref.child(id).child(textviewname.text.toString()))
                    cartdbref.child(id).child("Items").child(textviewname.text.toString()).removeValue()
                        .addOnCompleteListener() {
                            notifyItemRemoved(position)

                        }
                } else {
                    citem?.count = citem?.count.toString().toInt() - 1

                    textViewcount.text = citem?.count.toString()
                    textviewTprice.text =
                        ((citem?.product?.price.toString()
                            .toDouble()) * textViewcount.text.toString()
                            .toInt()).toString() + " LE"


                    cartdbref.child(id).child("Items").child(textviewname.text.toString()).setValue(citem)
                }
                checkout_price -= textviewprice.text.subSequence(
                    0,
                    textviewTprice.text.length - 3
                ).toString().toDouble()
                println(checkout_price)
                cartdbref.child(id).child("checkout_price").setValue(checkout_price)
            }
            //updatecart(mitems)

            println("====================")
            println(checkout_price)
            println("====================")
            //checkouttextview.text=checkout_price.toString()
        }


    }

    private fun getShop(): String {
        if (par.context.toString().contains("friends")) return "Friends"
        if (par.context.toString().contains("Pronto")) return "Pronto"
        if (par.context.toString().contains("ِAmSaadc")) return "3amsaad(c)"
        if (par.context.toString().contains("Laaroma")) return "L'aroma"
        if (par.context.toString().contains("Arabiata")) return "Arabiata"
        if (par.context.toString().contains("ِAmSaadb")) return "3amsaad(b)"

        return ""
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mitems.size
    }


}
