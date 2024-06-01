package com.example.fenbi.adapter

import com.example.fenbi.dataClass.Question

class PracticeAdapter(
    private var questionDataList: List<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val answerSheetAdapter: AnswerSheetAdapter,
) : PracticeBaseAdapter(questionDataList, userAnswerLists, answerSheetAdapter) {
    private var userAnswerIsSubmitList = ArrayList<Boolean>(userAnswerLists.size).apply {
        for (i in userAnswerLists.indices) {
            add(false)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if (position != questionDataList.size) {
            // 子RecyclerView表示选项布局
            holder.optionRecyclerView.adapter =
                PracticeOptionAdapter(
                    questionDataList[position],
                    userAnswerLists[position],
                    userAnswerIsSubmitList,
                    position
                ).apply {
                    answerSheetObserver = answerSheetAdapter.answerSheetObserver
                }
        }
    }
}