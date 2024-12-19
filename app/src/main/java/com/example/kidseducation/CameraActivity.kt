package com.example.kidseducation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
//import com.google.mlkit.vision.objects.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val previewView = findViewById<PreviewView>(R.id.camera_preview)

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                        processImage(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to start camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(options)

        objectDetector.process(inputImage)
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
                // Pastikan imageProxy ditutup
                imageProxy.close()
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
