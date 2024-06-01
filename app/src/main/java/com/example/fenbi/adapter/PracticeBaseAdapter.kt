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

    inner class ViewHolder(binding: ItemQuestionAreaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.questionTypeTv
        val contentTextView: TextView = binding.questionContentTv
        val optionRecyclerView: RecyclerView = binding.questionOptionRv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemQuestionAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = questionDataList.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == questionDataList.size) {
            // 答题卡界面隐藏用不到的组件
            holder.typeTextView.isVisible = false
            holder.contentTextView.isVisible = false
            holder.optionRecyclerView.apply {
                adapter = answerSheetAdapter
                layoutManager = GridLayoutManager(holder.itemView.context, 5).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            if (position == itemCount - 1) {
                                return spanCount
                            }
                            return 1
                        }
                    }
                }
                // 以免复用item时候错误添加itemDecoration
                if (itemDecorationCount == 0) {
                    addItemDecoration(AnswerSheetAdapter.ItemDecoration())
                } else if (itemDecorationCount == 1 && getItemDecorationAt(0) !is AnswerSheetAdapter.ItemDecoration) {
                    removeItemDecorationAt(0)
                    addItemDecoration(AnswerSheetAdapter.ItemDecoration())
                }
            }
        } else {
            // 显示题目类型和题目内容，在复用答题卡的holder中会被隐藏
            holder.typeTextView.apply {
                isVisible = true
                // 根据题目类型设置题目标签
                text = when (questionDataList[position].showType) {
                    1 -> "单选题"

                    2 -> "多选题"

                    3 -> "判断题"

                    else -> ""
                }
            }
            holder.contentTextView.apply {
                isVisible = true
                text = questionDataList[position].content // 题目内容
            }

            holder.optionRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                adapter =
                    BaseOptionAdapter(questionDataList[position], userAnswerLists[position], position).apply {
                        this.practiceUtils = this@PracticeBaseAdapter.practiceUtils
                        // 子RecyclerView表示选项布局
                        answerSheetObserver = answerSheetAdapter.answerSheetObserver
                    }
                // 以免复用item时候错误添加itemDecoration
                if (itemDecorationCount == 0) {
                    addItemDecoration(BaseOptionAdapter.ItemDecoration())
                } else if (itemDecorationCount == 1 && getItemDecorationAt(0) is AnswerSheetAdapter.ItemDecoration) {
                    removeItemDecorationAt(0)
                    addItemDecoration(BaseOptionAdapter.ItemDecoration())
                }
            }
        }
    }
}