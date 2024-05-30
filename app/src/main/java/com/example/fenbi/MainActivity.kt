package com.example.fenbi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fenbi.adapter.ChooseBankAdapter
import com.example.fenbi.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private var questionMode = ArrayList<Int>().apply {
        addAll(listOf(0, 0, 20, 1)) // 做题年份, 出题年份（无用）, 题目数量, 题目类型
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initQuestionMode()
        val chooseBankObserver = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.i("chooseBankObserver", "onSubscribe:$d")
            }

            override fun onError(e: Throwable) {
                Log.e("chooseBankObserver", "onError: ${e.message}")
            }

            override fun onComplete() {
                PracticeActivity.actionStart(this@MainActivity, questionMode)
                Log.i("chooseBankObserver", "onComplete")
            }

            override fun onNext(t: Int) {
                questionMode[3] = t // 题目类型
                Log.i("chooseBankObserver", "onNext: $t")
            }
        }
        binding.questionBankListRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ChooseBankAdapter().apply {
                this.chooseBankObserver = chooseBankObserver
            }
        }
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
            setOnValueChangedListener { picker, oldVal, newVal ->
                questionMode[2] = Integer.parseInt(displayedValues[newVal])

            }
        }
    }
}