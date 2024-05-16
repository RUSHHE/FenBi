package com.example.fenbi

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.fenbi.databinding.ActivityPracticeBinding

class PracticeActivity : ComponentActivity() {
    private lateinit var binding: ActivityPracticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }
}