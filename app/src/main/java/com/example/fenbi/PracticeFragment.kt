package com.example.fenbi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fenbi.adapter.ChooseBankAdapter
import com.example.fenbi.databinding.FragmentPracticeBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class PracticeFragment : Fragment() {
    private var _binding: FragmentPracticeBinding? = null
    // 只能在onCreateView与onDestroyView之间的生命周期里使用
    private val binding: FragmentPracticeBinding get() = _binding!!
    private var questionMode = ArrayList<Int>().apply {
        addAll(listOf(0, 0, 20, 1)) // 做题模式, 出题年份（无用）, 题目数量, 题目类型
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)

        initQuestionMode()
        val chooseBankObserver = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.i("chooseBankObserver", "onSubscribe:$d")
            }

            override fun onError(e: Throwable) {
                Log.e("chooseBankObserver", "onError: ${e.message}")
            }

            override fun onComplete() {
                PracticeActivity.actionStart(context!!, questionMode)
                Log.i("chooseBankObserver", "onComplete")
            }

            override fun onNext(t: Int) {
                questionMode[3] = t // 题目类型
                Log.i("chooseBankObserver", "onNext: $t")
            }
        }
        binding.questionBankListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ChooseBankAdapter().apply {
                this.chooseBankObserver = chooseBankObserver
            }
        }

        // 初始化自定义答题区域
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.customQuestionCard).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(p0: View, p1: Int) {
                    // TODO: 可以设置背景是否可被点击
                }

                override fun onSlide(p0: View, p1: Float) {
                    binding.questionBankListCl.alpha = 0.5f - p1
                }
            })
        }

        binding.customQuestionBtn.setOnClickListener {
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isHideable = true
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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