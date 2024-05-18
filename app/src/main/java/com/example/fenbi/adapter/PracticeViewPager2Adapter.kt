package com.example.fenbi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.OptionAreaBinding
import com.example.fenbi.databinding.QuestionAreaBinding
import com.example.fenbi.utils.RequestListenerUser
import com.google.android.material.button.MaterialButton

class PracticeViewPager2Adapter(
    private var questionDataList: List<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val requestListenerUser: RequestListenerUser
) :
    RecyclerView.Adapter<PracticeViewPager2Adapter.ViewHolder>() {
    inner class ViewHolder(binding: QuestionAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.questionTypeTv
        val contentTextView: TextView = binding.questionContentTv
        val optionRecyclerView: RecyclerView = binding.questionOptionRv
    }

    inner class OptionAdapter(
        private var question: Question,
        private var userAnswerList: ArrayList<Int>,
        private val parentPosition: Int
    ) :
        RecyclerView.Adapter<OptionAdapter.ViewHolder>() {
        inner class ViewHolder(binding: OptionAreaBinding) : RecyclerView.ViewHolder(binding.root) {
            val optionButton: MaterialButton = binding.optionBtn
            val optionTextView: TextView = binding.optionContentTv
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding: OptionAreaBinding =
                OptionAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                        if (userAnswerList.contains(position + 1)) {
                            userAnswerList.remove(position + 1)
                        } else {
                            if (userAnswerList.size >= 1) {
                                val index = userAnswerList[0] - 1
                                userAnswerList.removeAt(0)
                                notifyItemChanged(index)
                            }
                            userAnswerList.add(position + 1)
                            requestListenerUser.goToPage(parentPosition + 1)
                        }
                        Log.i("FenBi", "userAnswerList: $userAnswerList")
                    }

                    2 -> {
                        if (userAnswerList.contains(position + 1)) {
                            userAnswerList.remove(position + 1)
                        } else {
                            userAnswerList.add(position + 1)
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
            holder.optionButton.isChecked = userAnswerList.contains(position + 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            QuestionAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int = questionDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
            OptionAdapter(questionDataList[position], userAnswerLists[position], position)
    }
}