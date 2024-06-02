package com.example.fenbi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fenbi.adapter.AnswerSheetAdapter
import com.example.fenbi.adapter.ExamAdapter
import com.example.fenbi.adapter.PracticeAdapter
import com.example.fenbi.dataClass.QuestionResponseModel
import com.example.fenbi.databinding.ActivityPracticeBinding
import com.example.fenbi.utils.PracticeUtils
import com.example.fenbi.utils.QuestionBankUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.Date

class PracticeActivity : ComponentActivity() {
    private lateinit var binding: ActivityPracticeBinding
    private lateinit var questionMode: ArrayList<Int>

    companion object {
        fun actionStart(context: Context, questionMode: ArrayList<Int>) {
            val intent = Intent(context, PracticeActivity::class.java)
            intent.putIntegerArrayListExtra("questionMode", questionMode)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeBinding.inflate(layoutInflater)
        questionMode = intent.getIntegerArrayListExtra("questionMode") as ArrayList<Int>
        val view = binding.root

        setContentView(view)

        PracticeSingleton.questionDataList = ArrayList()
        val practiceObserver: Observer<Int> = object : Observer<Int> {
            override fun onNext(position: Int) {
                binding.practiceVp2.setCurrentItem(position, true)
                Log.i("PageRequestListenerUser", "onNext: $position")
            }

            override fun onSubscribe(d: Disposable) {
                Log.i("PageRequestListenerUser", "onSubscribe: $d")
            }

            override fun onError(e: Throwable) {
                Log.e("PageRequestListenerUser", "onError: $e")
            }

            override fun onComplete() {
                Log.i("PageRequestListenerUser", "onComplete: ")
            }
        }

        val practiceUtils = PracticeUtils(practiceObserver)
        var answerSheetAdapter: AnswerSheetAdapter
        // 答题卡提交事件的观察者
        val answerSheetSubmitObserver: Observer<Void> = object : Observer<Void> {
            override fun onSubscribe(d: Disposable) {
                Log.i("AnswerSheetSubmitObserver", "onSubscribe: $d")
            }

            override fun onError(e: Throwable) {
                Log.e("AnswerSheetSubmitObserver", "onError: $e")
            }

            override fun onComplete() {
                var totalTime: Long
                binding.practiceToolbar.timerUtils.apply {
                    totalTime = count
                }
                ReportActivity.actionStart(
                    context = this@PracticeActivity,
                    practiceType =
                    when (questionMode[3]) {
                        0 -> {
                            "习近平新时代中国特色社会主义思想概论"
                        }

                        1 -> {
                            "马原"
                        }

                        2 -> {
                            "毛泽东思想和中国特色社会主义理论体系概论"
                        }

                        3 -> {
                            "中国近代史纲要"
                        }

                        4 -> {
                            "思想道德与法治"
                        }

                        else -> {
                            ""
                        }
                    },
                    totalTime = totalTime,
                    submitTime = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(System.currentTimeMillis()))
                        .toString()
                )
                Log.i("AnswerSheetSubmitObserver", "onComplete")
                finish()
            }

            override fun onNext(t: Void) {
                Log.i("AnswerSheetSubmitObserver", "onNext")
            }
        }

        // 初始化底部的答题卡
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.answerSheetCv).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(p0: View, p1: Int) {
                    // TODO: 可以设置背景是否可被点击
                }

                override fun onSlide(p0: View, p1: Float) {
                    binding.practiceVp2.alpha = 0.5f - p1
                }
            })
        }

        val observer = object : Observer<QuestionResponseModel> {
            override fun onSubscribe(d: Disposable) {
                Log.i("QuestionGetService", "onSubscribe: $d")
            }

            override fun onError(e: Throwable) {
                Log.e("QuestionGetService", "onError: ${e.message}")
            }

            override fun onComplete() {
                Log.i("QuestionGetService", "onComplete")
            }

            override fun onNext(t: QuestionResponseModel) {
                // 存储问题和用户回答的单例
                PracticeSingleton.questionDataList!!.addAll(t.data.questions)
                PracticeSingleton.userAnswerLists =
                    MutableList(PracticeSingleton.questionDataList!!.size) { ArrayList() }

                // 初始化答题卡适配器
                answerSheetAdapter = AnswerSheetAdapter(
                    PracticeSingleton.userAnswerLists!!
                ).apply {
                    // 传入观察者
                    this.answerSheetSubmitObserver = answerSheetSubmitObserver
                    this.practiceUtils = practiceUtils
                }

                // 设置考试页面的适配器
                binding.practiceVp2.adapter =
                    if (questionMode[0] == 0) {
                        ExamAdapter(
                            PracticeSingleton.questionDataList!!,
                            PracticeSingleton.userAnswerLists!!,
                            answerSheetAdapter,
                        ).apply {
                            this@apply.practiceUtils = practiceUtils
                        }
                    } else {
                        PracticeAdapter(
                            PracticeSingleton.questionDataList!!,
                            PracticeSingleton.userAnswerLists!!,
                            answerSheetAdapter,
                        )
                    }

                binding.answerSheetRv.apply {
                    // 设置答题卡的布局
                    this@apply.layoutManager = GridLayoutManager(baseContext, 5).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                if (position == itemCount - 1) {
                                    return spanCount
                                }
                                return 1
                            }
                        }
                    }
                    adapter = answerSheetAdapter
                    addItemDecoration(AnswerSheetAdapter.ItemDecoration())
                }

                binding.answerSheetCloseBtn.setOnClickListener {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                Log.i("QuestionGetService", "onNext: $t")
            }
        }

        QuestionBankUtils(questionMode).subscribe(observer) // 订阅题库工具类获取的题库

        // 订阅工具栏，设置toolbar区域的观察者
        binding.practiceToolbar.toolbarObserver = object : Observer<Void> {
            override fun onSubscribe(d: Disposable) {
                Log.i("ToolbarObserver", "onSubscribe: $d")
            }

            override fun onError(e: Throwable) {
                Log.e("ToolbarObserver", "onError: $e")
            }

            override fun onComplete() {
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.isHideable = true
                Log.i("ToolbarObserver", "onComplete: ")
            }

            override fun onNext(t: Void) {
                Log.i("ToolbarObserver", "onNext: ")
            }
        }
    }
}