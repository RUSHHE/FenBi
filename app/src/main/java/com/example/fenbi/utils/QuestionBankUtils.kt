package com.example.fenbi.utils

import com.example.fenbi.QuestionGetService
import com.example.fenbi.dataClass.QuestionResponseModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class QuestionBankUtils(questionMode: ArrayList<Int>) {
    private var observable: Observable<QuestionResponseModel>

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://120.55.64.65:8056")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        val apiService = retrofit.create(QuestionGetService::class.java)

        // 获取题库
        observable = apiService.getQuestion(
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
    }

    fun subscribe(observer: Observer<QuestionResponseModel>) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}