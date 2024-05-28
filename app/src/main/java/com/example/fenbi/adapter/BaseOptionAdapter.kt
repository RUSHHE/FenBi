package com.example.fenbi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.ItemOptionBinding
import com.example.fenbi.utils.PracticeUtils
import com.google.android.material.button.MaterialButton
import io.reactivex.rxjava3.core.Observer

open class BaseOptionAdapter(
    private var question: Question,
    private var userAnswerList: ArrayList<Int>
) :
    RecyclerView.Adapter<BaseOptionAdapter.ViewHolder>() {
    var practiceUtils: PracticeUtils? = null
    var answerSheetObserver: Observer<Int>? = null
    inner class ViewHolder(binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        val optionButton: MaterialButton = binding.optionBtn
        val optionTextView: TextView = binding.optionContentTv
    }

    open class ItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: android.view.View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = 25
            outRect.bottom = 25
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemOptionBinding =
            ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int = if (question.showType == 3) {
        2
    } else {
        question.chooses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.optionButton.text = ('A' + position).toString()
        holder.optionButton.setOnClickListener {
            when (question.showType) {
                1, 3 -> {
                    if (userAnswerList.contains(position)) {
                        userAnswerList.remove(position)
                    } else {
                        if (userAnswerList.size >= 1) {
                            val index = userAnswerList[0] - 1
                            userAnswerList.removeAt(0)
                            notifyItemChanged(index)
                        }
                        userAnswerList.add(position)
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
        }
        when (question.showType) {
            1 -> {
                holder.optionTextView.text = question.chooses[position]
                // TODO 按钮的样式
            }

            2 -> {
                holder.optionTextView.text = question.chooses[position]
                // TODO 按钮的样式
            }

            3 -> {
                holder.optionTextView.text = if (position == 0) {
                    "正确"
                } else {
                    "错误"
                }
            }
        }
        holder.optionButton.isChecked = userAnswerList.contains(position)
    }
}