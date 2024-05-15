package com.example.fenbi.dataClass

data class QuestionResponseModel (
    val code: Long,
    val message: String,
    val data: Data
)

data class Data (
    val questions: List<Question>,
    val page: Page
)

data class Page (
    val current: Int,
    val pages: Int,
    val size: Int,
    val total: Int
)

data class Question (
    val id: Int,
    val content: String,
    val courseType: Int,
    val showType: Int,
    val chooses: List<String>,
    val answers: List<Int>
)