package com.example.fenbi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.QuestionAreaBinding

class FragmentBlankAdapter(private var questionDataList: List<Question>) :
    RecyclerView.Adapter<FragmentBlankAdapter.ViewHolder>() {
    inner class ViewHolder(binding: QuestionAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        val typeTextView: TextView = binding.questionTypeTv
        val contentTextView: TextView = binding.questionContentTv
        val optionRecyclerView: RecyclerView = binding.questionOptionRv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            QuestionAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int = questionDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        holder.contentTextView.text = questionDataList[position].content
    }
}