package com.example.fenbi

import com.example.fenbi.dataClass.QuestionResponseModel
import io.reactivex.rxjava3.core.Observable
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
    ): Observable<QuestionResponseModel>
}