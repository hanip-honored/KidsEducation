package com.example.kidseducation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SplashPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SplashFragment1()   // Fragment pertama
            1 -> SplashFragment2()   // Fragment kedua
            2 -> SplashFragment3()   // Fragment ketiga
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
