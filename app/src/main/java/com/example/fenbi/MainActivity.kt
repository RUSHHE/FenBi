package com.example.fenbi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fenbi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeViewPager.apply {
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int = 5

                override fun createFragment(position: Int): Fragment {
                    return PracticeFragment()
                }
            }
            isUserInputEnabled = false // 禁用滑动
        }

        binding.bottomNavigation.apply {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.practice_fragment -> {
                        binding.homeViewPager.currentItem = 0
                        true
                    }

                    R.id.lesson_fragment -> {
                        binding.homeViewPager.currentItem = 1
                        true
                    }

                    R.id.online_fragment -> {
                        binding.homeViewPager.currentItem = 2
                        true
                    }

                    R.id.discover_fragment -> {
                        binding.homeViewPager.currentItem = 3
                       true
                    }

                    R.id.about_me_fragment -> {
                        binding.homeViewPager.currentItem = 4
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }
}