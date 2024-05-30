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
import com.example.fenbi.dataClass.QuestionResponseModel
import com.example.fenbi.databinding.ActivityPracticeBinding
import com.example.fenbi.utils.PracticeUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val retrofit = Retrofit.Builder()
            .baseUrl("http://120.55.64.65:8056")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(QuestionGetService::class.java)

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
                Log.i("AnswerSheetSubmitObserver", PracticeSingleton.userAnswerLists.toString())
                ReportActivity.actionStart(this@PracticeActivity)
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

        // 获取题库
        val call = apiService.getQuestion(
            start = 1,
            size = questionMode[2],
            courseType =
            when (questionMode[3]) {
                // 马原
                1 -> {
                    1
                }

                // 思想道德与法治
                4 -> {
                    2
                }

                // API没有的统一马原
                else -> {
                    1
                }
            },
            showType = 1,
            isRand = 0
        )
        call.enqueue(object : retrofit2.Callback<QuestionResponseModel> {
            override fun onResponse(
                p0: retrofit2.Call<QuestionResponseModel>,
                p1: retrofit2.Response<QuestionResponseModel>
            ) {
                val questionResponseModel = p1.body()
                PracticeSingleton.questionDataList!!.addAll(questionResponseModel!!.data.questions)
                PracticeSingleton.userAnswerLists =
                    MutableList(PracticeSingleton.questionDataList!!.size) { ArrayList() }
                answerSheetAdapter = AnswerSheetAdapter(
                    PracticeSingleton.userAnswerLists!!
                ).apply {
                    // 传入观察者
                    this.answerSheetSubmitObserver = answerSheetSubmitObserver
                    this.practiceUtils = practiceUtils
                }
                binding.practiceVp2.adapter =
                    ExamAdapter(
                        PracticeSingleton.questionDataList!!,
                        PracticeSingleton.userAnswerLists!!,
                        answerSheetAdapter,
                    ).apply {
                        this.practiceUtils = practiceUtils
                    }

                // 设置答题卡的布局
                val layoutManager = GridLayoutManager(baseContext, 5).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            if (position == this@apply.itemCount - 1) {
                                return spanCount
                            }
                            return 1
                        }
                    }
                }

                binding.answerSheetRv.apply {
                    this@apply.layoutManager = layoutManager
                    adapter = answerSheetAdapter
                    addItemDecoration(AnswerSheetAdapter.ItemDecoration())
                }

                binding.answerSheetCloseBtn.setOnClickListener {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                Log.i("获取题库", "onResponse: $questionResponseModel")
            }

            override fun onFailure(p0: retrofit2.Call<QuestionResponseModel>, p1: Throwable) {
                Log.e("获取题库", "onFailure: " + p1.message)
            }
        })
    }
}