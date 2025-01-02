package com.example.kidseducation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ObjectDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)

        val buttonCamera = findViewById<Button>(R.id.button_camera)
        val buttonGallery = findViewById<Button>(R.id.button_gallery)

        // Navigasi ke live detection
        buttonCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // Pilih gambar dari galeri
        buttonGallery.setOnClickListener {
            openGallery()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            processImage(bitmap)
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun processImage(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE) // Untuk gambar statis
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
                if (detectedObjects.isNotEmpty()) {
                    val label = detectedObjects.first().labels.firstOrNull()?.text ?: "Unknown"
                    navigateToDetail(label, "Description not available", null)
                } else {
                    Toast.makeText(this, "No object detected", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToDetail(objectName: String, description: String, imageUrl: String?) {
        val intent = Intent(this, ObjectDetailActivity::class.java)
        intent.putExtra("object_name", objectName)
        intent.putExtra("object_description", description)
        intent.putExtra("object_image_url", imageUrl ?: "https://via.placeholder.com/150")
        startActivity(intent)
    }
}