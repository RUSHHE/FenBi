package com.example.fenbi.adapter

import com.example.fenbi.dataClass.Question

class PracticeAdapter(
    private var questionDataList: List<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val answerSheetAdapter: AnswerSheetAdapter,
) : PracticeBaseAdapter(questionDataList, userAnswerLists, answerSheetAdapter) {

}