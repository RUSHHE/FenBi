package com.example.fenbi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.ItemQuestionAreaBinding
import com.example.fenbi.utils.PracticeUtils

open class PracticeBaseAdapter(
    private var questionDataList: List<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val answerSheetAdapter: AnswerSheetAdapter
) :
    RecyclerView.Adapter<PracticeBaseAdapter.ViewHolder>() {
    var practiceUtils: PracticeUtils? = null

    inner class ViewHolder(binding: ItemQuestionAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.questionTypeTv
        val contentTextView: TextView = binding.questionContentTv
        val optionRecyclerView: RecyclerView = binding.questionOptionRv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemQuestionAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int = questionDataList.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == questionDataList.size) {
            // 答题卡界面隐藏用不到的组件
            holder.typeTextView.isVisible = false
            holder.contentTextView.isVisible = false
            holder.optionRecyclerView.adapter = answerSheetAdapter
            // 以免复用item时候错误添加itemDecoration
            if (holder.optionRecyclerView.itemDecorationCount == 0) {
                holder.optionRecyclerView.addItemDecoration(AnswerSheetAdapter.ItemDecoration())
            } else if (holder.optionRecyclerView.itemDecorationCount == 1 && holder.optionRecyclerView.getItemDecorationAt(0) !is AnswerSheetAdapter.ItemDecoration) {
                holder.optionRecyclerView.removeItemDecorationAt(0)
                holder.optionRecyclerView.addItemDecoration(AnswerSheetAdapter.ItemDecoration())
            }
            val layoutManager = GridLayoutManager(holder.itemView.context, 5)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position == layoutManager.itemCount - 1) {
                        return layoutManager.spanCount
                    }
                    return 1
                }
            }
            holder.optionRecyclerView.layoutManager = layoutManager
        } else {
            // 显示题目类型和题目内容，在复用答题卡的holder中会被隐藏
            holder.typeTextView.isVisible = true
            holder.contentTextView.isVisible = true
            // 根据题目类型设置题目标签
            when (questionDataList[position].showType) {
                1 -> {
                    holder.typeTextView.text = "单选题"
                }

                2 -> {
                    holder.typeTextView.text = "多选题"
                }

                3 -> {
                    holder.typeTextView.text = "判断题"
                }
            }
            // 题目内容
            holder.contentTextView.text = questionDataList[position].content

            val layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.optionRecyclerView.layoutManager = layoutManager
            // 子RecyclerView表示选项布局
            holder.optionRecyclerView.adapter =
                BaseOptionAdapter(questionDataList[position], userAnswerLists[position]).apply {
                    this@apply.practiceUtils = this@PracticeBaseAdapter.practiceUtils
                    answerSheetObserver = answerSheetAdapter.answerSheetObserver
                }
            // 以免复用item时候错误添加itemDecoration
            if (holder.optionRecyclerView.itemDecorationCount == 0) {
                holder.optionRecyclerView.addItemDecoration(BaseOptionAdapter.ItemDecoration())
            } else if (holder.optionRecyclerView.itemDecorationCount == 1 && holder.optionRecyclerView.getItemDecorationAt(0) is AnswerSheetAdapter.ItemDecoration) {
                holder.optionRecyclerView.removeItemDecorationAt(0)
                holder.optionRecyclerView.addItemDecoration(BaseOptionAdapter.ItemDecoration())
            }
        }
    }
}