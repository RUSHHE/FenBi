package com.example.fenbi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.fenbi.dataClass.Question
import com.example.fenbi.dataClass.QuestionResponseModel
import com.example.fenbi.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://120.55.64.65:8056")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(QuestionGetService::class.java)

        val call = apiService.getQuestion(1, 5, 1, 1, 0)
        call.enqueue(object : retrofit2.Callback<QuestionResponseModel> {
            override fun onResponse(
                p0: retrofit2.Call<QuestionResponseModel>,
                p1: retrofit2.Response<QuestionResponseModel>
            ) {
                val questionResponseModel = p1.body()
                val questionDataList: List<Question>? = questionResponseModel?.data?.questions
                Log.i("获取题库", "onResponse: " + questionResponseModel.toString())
            }

            override fun onFailure(p0: retrofit2.Call<QuestionResponseModel>, p1: Throwable) {
                Log.e("获取题库", "onFailure: " + p1.message)
            }

        })

        startActivity(Intent(this, PracticeActivity::class.java))
    }
}