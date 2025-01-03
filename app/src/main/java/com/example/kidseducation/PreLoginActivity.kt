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

        // Inisialisasi tombol
        buttonMasuk = findViewById(R.id.buttonMasuk)
        buttonDaftar = findViewById(R.id.buttonDaftar)

        // Event klik tombol Masuk
        buttonMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Event klik tombol Daftar
        buttonDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
