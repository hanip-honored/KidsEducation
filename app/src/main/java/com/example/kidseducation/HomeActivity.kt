package com.example.kidseducation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class HomeActivity : AppCompatActivity() {
    private  lateinit var firebaseUser: FirebaseUser

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textName = findViewById<TextView>(R.id.name)

//        firebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//        if (firebaseUser.displayName != null){
//            textName?.setText(firebaseUser.displayName)
//        } else {
//            textName.setText("Login Gagal")
//        }
//
//        findViewById<Button>(R.id.logout).setOnClickListener {
//            Firebase.auth.signOut()
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }
}