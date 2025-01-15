package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PreLoginActivity : AppCompatActivity() {

    private lateinit var buttonMasuk: Button
    private lateinit var buttonDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)

        buttonMasuk = findViewById(R.id.buttonMasuk)
        buttonDaftar = findViewById(R.id.buttonDaftar)

        buttonMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonDaftar.setOnClickListener {
            val intent = Intent(this, PreRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
