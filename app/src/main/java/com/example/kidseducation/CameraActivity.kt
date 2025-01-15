package com.example.kidseducation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
//import com.google.mlkit.vision.objects.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts


class CameraActivity : AppCompatActivity() {
    private lateinit var objectDetectorCamera: ObjectDetector
    private lateinit var objectDetectorGallery: ObjectDetector
    private lateinit var previewView: PreviewView
    private val CAMERA_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.camera_preview)

        // Tombol untuk membuka galeri
        val textImage: TextView = findViewById(R.id.text_image)
        textImage.setOnClickListener {
            val intentKuis = Intent(this, ObjectDetectionActivity::class.java)
            startActivity(intentKuis)
        }

        // Tombol close
        val btnClose: ImageButton = findViewById(R.id.button_close)
        btnClose.setOnClickListener {
            val intentKuis = Intent(this, HomeActivity::class.java)
            startActivity(intentKuis)
            finish()
        }

        checkCameraPermission()
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

//            val imageAnalyzer = ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build()
//                .also {
//                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
//                        processImageFromCamera(imageProxy)
//                    }
//                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this,
//                    cameraSelector,
//                    preview,
//                    imageAnalyzer
//                )
//            } catch (e: Exception) {
//                Toast.makeText(this, "Failed to start camera", Toast.LENGTH_SHORT).show()
//            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageFromCamera(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

        objectDetectorCamera.process(inputImage)
            .addOnSuccessListener { detectedObjects ->
                if (detectedObjects.isNotEmpty()) {
                    val label = detectedObjects.first().labels.firstOrNull()?.text ?: "Unknown"
                    navigateToDetail(label, "Description not available", null)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            processImageFromGallery(bitmap)
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun processImageFromGallery(bitmap: Bitmap) {
        val galleryOptions = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(galleryOptions)

        val inputImage = InputImage.fromBitmap(bitmap, 0)

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

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
