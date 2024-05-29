package com.example.fenbi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.fenbi.databinding.ActivityMainBinding


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private var questionMode: MutableList<Int> = listOf(0, 0, 5).toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initQuestionMode()
        startActivity(Intent(this, PracticeActivity::class.java))
    }

    private fun initQuestionMode() {
        // 出题模式初始化
        when (questionMode[0]) {
            0 -> {
                binding.customQuestionLayout.examModeBtn.isChecked = true
            }

            1 -> {
                binding.customQuestionLayout.examModeBtn.isChecked = true
            }
        }
        // 出题年份初始化
        when (questionMode[1]) {
            0 -> {
                binding.customQuestionLayout.questionYearAnyBtn.isChecked = true
            }

            1 -> {
                binding.customQuestionLayout.questionYearFiveBtn.isChecked = true
            }

            2 -> {
                binding.customQuestionLayout.questionYearTenBtn.isChecked = true
            }
        }
        binding.customQuestionLayout.examModeBtn.setOnClickListener {
            if (questionMode[0] != 0) {
                binding.customQuestionLayout.practiceModeBtn.isChecked = false
                questionMode[0] = 0
            } else {
                binding.customQuestionLayout.examModeBtn.isChecked = true
            }
        }
        binding.customQuestionLayout.practiceModeBtn.setOnClickListener {
            if (questionMode[0] != 1) {
                binding.customQuestionLayout.examModeBtn.isChecked = false
                questionMode[0] = 1
            } else {
                binding.customQuestionLayout.practiceModeBtn.isChecked = true
            }
        }
        // 初始化出题数量选择器
        binding.customQuestionLayout.questionCountNp.apply {
            wrapSelectorWheel = false
            displayedValues = arrayOf("5", "10", "15", "20", "25", "30", "35", "40")
            minValue = 0
            maxValue = displayedValues.size - 1
            value = (displayedValues.size - 1) / 2
        }
    }
}