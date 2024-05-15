package com.example.fenbi

import com.example.fenbi.dataClass.QuestionResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionGetService {
    @GET("question/byPage")
    fun getQuestion(
        @Query("start") start: Int,
        @Query("size") size: Int,
        @Query("courseType") courseType: Int,
        @Query("showType") showType: Int,
        @Query("isRand") isRand: Int
    ): Call<QuestionResponseModel>
}