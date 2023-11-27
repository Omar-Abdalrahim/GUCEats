package com.example.guceats.LoginOrRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.guceats.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var reg: Button
    private lateinit var logintxtbtn: TextView
    private lateinit var Email: EditText
    private lateinit var Pass: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        reg = findViewById(R.id.registerbtn)
        logintxtbtn = findViewById(R.id.logxt)
        Email = findViewById(R.id.EmailAddresstxtreg)
        Pass = findViewById(R.id.Passwordtxtreg)


        fun registerUser(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    //println(email + password)
                    if (task.isSuccessful) {
                        // Registration successful
                        val i = Intent(this, Login::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        // Registration failed
                        Log.w("Registration", "createUserWithEmail:failure", task.exception)
                    }
                }
        }

        reg.setOnClickListener(View.OnClickListener { view ->
            val e: String = Email.text.toString()
            val p: String = Pass.text.toString()
            if (TextUtils.isEmpty(e) || TextUtils.isEmpty(p)) {
                Toast.makeText(
                    this@Register,
                    "Both Email & Pass Can not be Empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            registerUser(e, p)
        })
        logintxtbtn.setOnClickListener { view ->
            val i = Intent(this, Login::class.java)
            startActivity(i)
            finish()
        }
    }
}