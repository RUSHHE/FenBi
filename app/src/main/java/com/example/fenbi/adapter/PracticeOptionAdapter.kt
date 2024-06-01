package com.example.fenbi.adapter

import androidx.core.content.ContextCompat
import com.example.fenbi.R
import com.example.fenbi.dataClass.Question
import io.reactivex.rxjava3.core.Observable

class PracticeOptionAdapter(
    private var question: Question,
    private var userAnswerList: ArrayList<Int>,
    private var userAnswerIsSubmit: ArrayList<Boolean>,
    private val parentPosition: Int
) : BaseOptionAdapter(question, userAnswerList, parentPosition) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.optionButton.apply {
            if (userAnswerIsSubmit[parentPosition] && question.answers.contains(position)) {
                isChecked = true
            }
            setOnClickListener {
                // 提交后不能更改
                if (userAnswerIsSubmit[parentPosition]) {
                    isChecked = !isChecked
                    return@setOnClickListener
                }

                userAnswerList.apply {
                    when (question.showType) {
                        1, 3 -> {
                            add(position)
                            userAnswerIsSubmit[parentPosition] = true
                            showCorrectAnswer()
                        }

                        2 -> {
                            if (contains(position)) {
                                remove(position)
                            } else {
                                add(position)
                            }
                        }
                    }
                }
                val answerSheetObservable = Observable.just(parentPosition)
                if (answerSheetObserver != null) {
                    answerSheetObservable.subscribe(answerSheetObserver!!)
                }
            }
            if (question.answers.contains(position)) {
                backgroundTintList =
                    ContextCompat.getColorStateList(
                        context,
                        R.color.button_correct_option_background_tint
                    )
                setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.button_correct_option_text_color
                    )
                )
                setStrokeColorResource(R.color.button_correct_option_stroke_color)
            } else {
                backgroundTintList =
                    ContextCompat.getColorStateList(
                        context,
                        R.color.button_wrong_option_background_tint
                    )
                setTextColor(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.button_wrong_option_text_color
                    )
                )
                setStrokeColorResource(R.color.button_wrong_option_stroke_color)
            }
        }
    }

    private fun showCorrectAnswer() {
        for (i in question.answers) {
            notifyItemChanged(i)
        }
    }
}