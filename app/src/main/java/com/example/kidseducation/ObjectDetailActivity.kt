package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ObjectDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detail)

        val objectName = intent.getStringExtra("object_name")
        val objectDescription = intent.getStringExtra("object_description")
        val objectImageUrl = intent.getStringExtra("object_image_url")

        val objectNameView = findViewById<TextView>(R.id.object_name)
        val objectDescriptionView = findViewById<TextView>(R.id.object_description)
        val objectImageView = findViewById<ImageView>(R.id.object_image)

        objectNameView.text = objectName
        objectDescriptionView.text = objectDescription

        Picasso.get().load(objectImageUrl).into(objectImageView)

        val btnNext = findViewById<Button>(R.id.btnNext)

        btnNext.setOnClickListener {
            val intent = Intent(this, CongratulationActivity::class.java)
            startActivity(intent)
        }
    }
}
