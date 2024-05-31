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

    override fun getItemCount(): Int =
        if (question.showType == 3) {
            2
        } else {
            question.chooses.size
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.optionButton.apply {
            text = ('A' + position).toString()
            isChecked = userAnswerList.contains(position)
            setOnClickListener {
                userAnswerList.apply {
                    if (contains(position)) {
                        remove(position)
                    } else {
                        // 单选和多选的情况
                        if ((question.showType == 1 || question.showType == 3) && size >= 1) {
                            val index = get(0)
                            removeAt(0)
                            notifyItemChanged(index)
                        }
                        add(position)
                    }
                }
            }
        }
        holder.optionTextView.text = when (question.showType) {
            1, 2 -> question.chooses[position]

            3 -> {
                if (position == 0) {
                    "正确"
                } else {
                    "错误"
                }
            }

            else -> ""
        }
    }
}