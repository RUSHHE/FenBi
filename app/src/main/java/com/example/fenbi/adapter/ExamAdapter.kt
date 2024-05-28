package com.example.fenbi.adapter

import com.example.fenbi.dataClass.Question

class ExamAdapter(
    private var questionDataList: List<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val answerSheetAdapter: AnswerSheetAdapter,
) : PracticeBaseAdapter(questionDataList, userAnswerLists, answerSheetAdapter) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position != questionDataList.size) {
            // 子RecyclerView表示选项布局
            holder.optionRecyclerView.adapter =
                ExamOptionAdapter(questionDataList[position], userAnswerLists[position], position).apply {
                    this@apply.practiceUtils = super.practiceUtils
                    answerSheetObserver = answerSheetAdapter.answerSheetObserver
                }
        }
    }
}