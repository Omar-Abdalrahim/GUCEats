package com.example.guceats.LoginOrRegister

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.guceats.Home
import com.example.guceats.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {
    private lateinit var login: Button
    private lateinit var regtxtbtn: TextView
    private lateinit var Email: EditText
    private lateinit var Pass: EditText
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        login = findViewById(R.id.loginbtn)
        regtxtbtn = findViewById(R.id.regxt)
        Email = findViewById(R.id.EmailAddresstxtlog)
        Pass = findViewById(R.id.Passwordtxtlog)

        fun loginUser(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"Successfully logged in!", Toast.LENGTH_SHORT).show()
                        val i = Intent(this, Home::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        // Login failed
                        Toast.makeText(applicationContext,"signInWithEmail:failure!", Toast.LENGTH_SHORT).show()
                        Log.w("Login", "signInWithEmail:failure", task.exception)
                    }
                }
        }
            login.setOnClickListener(View.OnClickListener { view ->
                val e: String = Email.text.toString()
                val p: String = Pass.text.toString()
                if (TextUtils.isEmpty(e) || TextUtils.isEmpty(p)) {
                    Toast.makeText(this@Login, "Both Email & Pass Can not be Empty", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                loginUser(e,p) })
            regtxtbtn.setOnClickListener { view ->
                val i = Intent(this, Register::class.java)
                startActivity(i)
                finish()
            }


    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
                val i = Intent(this, Home::class.java)
                startActivity(i)
                finish()
        }
    }
}
