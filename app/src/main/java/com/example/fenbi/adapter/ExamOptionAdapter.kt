package com.example.fenbi.adapter

import android.util.Log
import com.example.fenbi.dataClass.Question
import io.reactivex.rxjava3.core.Observable

class ExamOptionAdapter(
    private var question: Question,
    private var userAnswerList: ArrayList<Int>,
    private val parentPosition: Int
) : BaseOptionAdapter(question, userAnswerList) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.optionButton.setOnClickListener {
            when (question.showType) {
                1, 3 -> {
                    Log.i("ExamOptionAdapter", "userAnswerList: $userAnswerList")
                    if (userAnswerList.contains(position)) {
                        userAnswerList.remove(position)
                    } else {
                        if (userAnswerList.size >= 1) {
                            val index = userAnswerList[0] - 1
                            userAnswerList.removeAt(0)
                            notifyItemChanged(index)
                        }
                        userAnswerList.add(position)
                        // 考试类的单选题要跳转
                        practiceUtils?.goToPage(parentPosition + 1)
                    }
                }

                2 -> {
                    if (userAnswerList.contains(position)) {
                        userAnswerList.remove(position)
                    } else {
                        userAnswerList.add(position)
                    }
                }
            }
            // 考试模式单选判断题换页
            val answerSheetObservable = Observable.create {
                it.onNext(parentPosition)
            }
            // 只有答题卡页面未被销毁才订阅
            if (answerSheetObserver != null) {
                answerSheetObservable.subscribe(answerSheetObserver!!)
            }
        }
    }
}