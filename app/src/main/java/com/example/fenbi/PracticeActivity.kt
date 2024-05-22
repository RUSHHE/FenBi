package com.example.fenbi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.fenbi.adapter.PracticeViewPager2Adapter
import com.example.fenbi.dataClass.Question
import com.example.fenbi.dataClass.QuestionResponseModel
import com.example.fenbi.databinding.ActivityPracticeBinding
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PracticeActivity : ComponentActivity() {
    private lateinit var binding: ActivityPracticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://120.55.64.65:8056")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(QuestionGetService::class.java)

        val questionDataList: ArrayList<Question> = ArrayList()
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

        val call = apiService.getQuestion(1, 5, 1, 1, 0)
        call.enqueue(object : retrofit2.Callback<QuestionResponseModel> {
            override fun onResponse(
                p0: retrofit2.Call<QuestionResponseModel>,
                p1: retrofit2.Response<QuestionResponseModel>
            ) {
                val questionResponseModel = p1.body()
                questionDataList.addAll(questionResponseModel!!.data.questions)
                val userAnswerLists = MutableList(questionDataList.size) { ArrayList<Int>() }
                binding.practiceVp2.adapter =
                    PracticeViewPager2Adapter(questionDataList, userAnswerLists, practiceObserver)
                Log.i("获取题库", "onResponse: $questionResponseModel")
            }

            override fun onFailure(p0: retrofit2.Call<QuestionResponseModel>, p1: Throwable) {
                Log.e("获取题库", "onFailure: " + p1.message)
            }
        })
    }
}