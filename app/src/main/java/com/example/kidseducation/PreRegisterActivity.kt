package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PreRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pre_register)

        val txtNama: EditText = findViewById(R.id.TextNama)
        val buttonNext: Button = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener {
            val nickname = txtNama.text.toString().trim()

            if (nickname.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra("NICKNAME", nickname)
                startActivity(intent)
            }
        }

    }
}