package com.example.fenbi.adapter

import com.example.fenbi.dataClass.Question
import io.reactivex.rxjava3.core.Observable

class ExamOptionAdapter(
    private var question: Question,
    private var userAnswerList: ArrayList<Int>,
    private val parentPosition: Int
) : BaseOptionAdapter(question, userAnswerList, parentPosition) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.optionButton.apply {
            setOnClickListener {
                userAnswerList.apply {
                    if (contains(position)) {
                        remove(position)
                    } else {
                        // 单选和多选的情况
                        if (question.showType == 1 || question.showType == 3) {
                            if (size >= 1) {
                                val index = get(0)
                                removeAt(0)
                                notifyItemChanged(index)
                            }
                            practiceUtils?.goToPage(parentPosition + 1)
                        }
                        add(position)
                    }
                    val answerSheetObservable = Observable.just(parentPosition)
                    // 只有答题卡页面未被销毁才订阅
                    if (answerSheetObserver != null) {
                        answerSheetObservable.subscribe(answerSheetObserver!!)
                    }
                }
            }
        }
    }
}