plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.kidseducation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kidseducation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    androidResources {
        noCompress += "tflite";
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    // Firebase dan Google Sign-In
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Authentication dan Analytics
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics")

    // Google Sign In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("me.relex:circleindicator:2.1.6")

    // ML kit firebase
    implementation ("com.google.mlkit:object-detection:17.0.2")

    // Picasso untuk memuat gambar online
    implementation ("com.squareup.picasso:picasso:2.8")

    // CameraX
    implementation ("androidx.camera:camera-core:1.4.0")
    implementation ("androidx.camera:camera-camera2:1.4.0")
    implementation ("androidx.camera:camera-lifecycle:1.4.0")
    implementation ("androidx.camera:camera-view:1.4.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    //TensorFlow Lite
//    implementation ("org.tensorflow:tensorflow-lite:2.16.1")
//    implementation ("org.tensorflow:tensorflow-lite-task-vision:2.16.1")
//    implementation ("org.tensorflow:tensorflow-lite-gpu:2.16.1")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.8")
    implementation ("com.google.android.material:material:1.9.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.litert)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.litert.support.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}