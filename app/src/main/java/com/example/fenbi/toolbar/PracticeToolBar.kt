package com.example.fenbi.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fenbi.databinding.ToolbarPracticeBinding
import com.example.fenbi.utils.TimerUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

class PracticeToolBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: ToolbarPracticeBinding =
        ToolbarPracticeBinding.inflate(LayoutInflater.from(context), this, true)
    var toolbarObserver: Observer<Void>? = null
    val timerUtils = TimerUtils()
    var millisUntilFinished: Long = 0
    private var isStop = false

    init {
        binding.closeBtn.setOnClickListener {
            Toast.makeText(context, "关闭", Toast.LENGTH_SHORT).show()
        }
        binding.answerSheetBtn.setOnClickListener {
            val toolbarObservable = Observable.empty<Void>()
            if (toolbarObserver != null) {
                toolbarObservable.subscribe(toolbarObserver!!)
            }
        }
        timerUtils.startIntervalTimer(object : TimerUtils.TimerListener {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                binding.counterTv.text =
                    String.format("%02d:%02d", millisUntilFinished / 60, millisUntilFinished % 60)
            }

            override fun onFinish() {

            }
        })
        // 计时器按钮，按下暂停答题
        binding.counterBtn.setOnClickListener {
            if (!isStop) {
                millisUntilFinished += timerUtils.count
                timerUtils.stopAllTimers()
                isStop = true
            } else {
                timerUtils.startIntervalTimer(object : TimerUtils.TimerListener {
                    @SuppressLint("DefaultLocale")
                    override fun onTick(millisUntilFinished: Long) {
                        binding.counterTv.text =
                            String.format("%02d:%02d", (this@PracticeToolBar.millisUntilFinished + millisUntilFinished) / 60, (this@PracticeToolBar.millisUntilFinished + millisUntilFinished) % 60)
                    }

                    override fun onFinish() {

                    }
                })
                isStop = false
            }
        }
    }
}