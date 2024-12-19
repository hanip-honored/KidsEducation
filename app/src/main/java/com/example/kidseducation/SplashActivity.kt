package com.example.kidseducation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kidseducation.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek apakah splash screen sudah pernah ditampilkan
//        if (getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//                .getBoolean("splash_shown", false)) {
//            // Langsung ke MainActivity jika sudah pernah ditampilkan
//            startActivity(Intent(this, RegisterActivity::class.java))
//            finish()
//            return
//        }

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.adapter = SplashPagerAdapter(this)

        // Setup indicator
        val indicator = binding.indicator
        indicator.setViewPager(viewPager)

    }
}