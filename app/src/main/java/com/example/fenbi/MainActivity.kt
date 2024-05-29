package com.example.fenbi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.fenbi.databinding.ActivityMainBinding


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this, PracticeActivity::class.java))
    }
}