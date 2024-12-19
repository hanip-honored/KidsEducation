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

    private lateinit var retrofit: Retrofit
    private lateinit var wikipediaApi: WikipediaApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)

        retrofit = Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/w/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        wikipediaApi = retrofit.create(WikipediaApi::class.java)

        val buttonCamera = findViewById<Button>(R.id.button_camera)
        val buttonGallery = findViewById<Button>(R.id.button_gallery)

        buttonCamera.setOnClickListener { openCamera() }
        buttonGallery.setOnClickListener { openGallery() }
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
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
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
                for (detectedObject in detectedObjects) {
                    val objectName = detectedObject.labels.firstOrNull()?.text ?: "Unknown"
                    Toast.makeText(this, "Detected: $objectName", Toast.LENGTH_SHORT).show()

                    fetchObjectDetails(objectName)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Detection Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchObjectDetails(objectName: String) {
        wikipediaApi.searchObject(title = objectName)
            .enqueue(object : Callback<WikipediaResponse> {
                override fun onResponse(call: Call<WikipediaResponse>, response: Response<WikipediaResponse>) {
                    if (response.isSuccessful) {
                        val description = response.body()?.query?.pages?.values?.firstOrNull()?.extract
                            ?: "Description not available"
                        val imageUrl = "https://via.placeholder.com/200"

                        val intent = Intent(this@ObjectDetectionActivity, ObjectDetailActivity::class.java).apply {
                            putExtra("object_name", objectName)
                            putExtra("object_description", description)
                            putExtra("object_image_url", imageUrl)
                        }
                        startActivity(intent)
                    } else {
                        Log.e("API Error", "Failed to fetch object details")
                        Toast.makeText(this@ObjectDetectionActivity, "Failed to fetch details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WikipediaResponse>, t: Throwable) {
                    Log.e("API Failure", "Network error: ${t.message}")
                    Toast.makeText(this@ObjectDetectionActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    interface WikipediaApi {
        @GET("api.php")
        fun searchObject(
            @Query("action") action: String = "query",
            @Query("format") format: String = "json",
            @Query("prop") prop: String = "extracts",
            @Query("titles") title: String
        ): Call<WikipediaResponse>
    }

    data class WikipediaResponse(val query: QueryResult)
    data class QueryResult(val pages: Map<String, PageDetails>)
    data class PageDetails(val extract: String)
}