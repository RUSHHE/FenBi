package com.example.fenbi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.R
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.ItemExamStatusBinding
import com.example.fenbi.databinding.ItemReportBinding
import com.example.fenbi.databinding.ItemReportOverviewBinding
import com.example.fenbi.databinding.ItemReportOverviewContentBinding
import com.google.android.material.card.MaterialCardView

class ReportAdapter(
    var questionDataList: ArrayList<Question>,
    private var userAnswerLists: List<ArrayList<Int>>,
    private val practiceType: String,
    private var totalTime: String,
    private var submitTime: String
) : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
    private var totalAnswer: Int = 0
    private var correctAnswer: Int = 0
    private var wrongAnswer: Int = 0

    init {
        totalAnswer = questionDataList.size
        for (i in 0 until totalAnswer) {
            if (questionDataList[i].answers.toSet() == userAnswerLists[i].toSet()) {
                correctAnswer++
            } else if (userAnswerLists[i].isNotEmpty()) {
                wrongAnswer++
            }
        }
    }

    inner class ViewHolder(binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {
        val reportTypeIv: ImageView = binding.reportTypeIv
        val reportTypeTv: TextView = binding.reportTypeTv
        val reportDescriptionRv: RecyclerView = binding.reportDescriptionRv
        val reportContentCv: MaterialCardView = binding.reportContentCv
        val reportContentRv: RecyclerView = binding.reportContentRv
    }

    inner class OverviewReportContentAdapter :
        RecyclerView.Adapter<OverviewReportContentAdapter.ViewHolder>() {
        inner class ViewHolder(binding: ItemReportOverviewContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val reportContentTitle: TextView = binding.overviewContentTitle
            val reportContentValue: TextView = binding.overviewContentValue
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemReportOverviewContentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int = 2

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                0 -> {
                    holder.reportContentTitle.text = "练习类型："
                    holder.reportContentValue.text = practiceType
                }

                1 -> {
                    holder.reportContentTitle.text = "交卷时间："
                    holder.reportContentValue.text = submitTime
                }
            }
        }
    }

    inner class ExamStatusAdapter(
        private val totalAnswer: Int,
        private val correctAnswer: Int,
        private val wrongAnswer: Int
    ) : RecyclerView.Adapter<ExamStatusAdapter.ViewHolder>() {

        inner class ViewHolder(binding: ItemExamStatusBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val statusDescriptionTv = binding.statusDescriptionTv
            val statusContentTv = binding.statusContentTv
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                ItemExamStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int = if (correctAnswer + wrongAnswer == totalAnswer) {
            4
        } else {
            5
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                0 -> {
                    holder.statusDescriptionTv.text = "一共"
                    holder.statusContentTv.text = totalAnswer.toString()
                }

                1 -> {
                    holder.statusDescriptionTv.text = "答对"
                    holder.statusContentTv.text = correctAnswer.toString()
                }

                2 -> {
                    holder.statusDescriptionTv.text = "答错"
                    holder.statusContentTv.text = wrongAnswer.toString()
                }

                3 -> {
                    if (itemCount == 4) {
                        holder.statusDescriptionTv.text = "总用时"
                        holder.statusContentTv.text = "${totalTime}秒"
                    } else {
                        holder.statusDescriptionTv.text = "未答"
                        holder.statusContentTv.text =
                            (totalAnswer - correctAnswer - wrongAnswer).toString()
                    }
                }

                4 -> {
                    holder.statusDescriptionTv.text = "总用时"
                    holder.statusContentTv.text = "${totalTime}秒"
                }
            }
        }
    }

    inner class ReportOverviewAdapter : RecyclerView.Adapter<ReportOverviewAdapter.ViewHolder>() {
        inner class ViewHolder(binding: ItemReportOverviewBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val progressBar = binding.overviewProgressBar
            val correctTextTv = binding.overviewCorrectText
            val correctNumberTv = binding.overviewCorrectNumber
            val totalNumberTv = binding.overviewTotalNumber
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ReportOverviewAdapter.ViewHolder {
            val binding = ItemReportOverviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ReportOverviewAdapter.ViewHolder, position: Int) {
            holder.progressBar.progress = (correctAnswer.toFloat() / totalAnswer.toFloat()) * 100
            holder.correctTextTv.text = "答对"
            holder.correctNumberTv.text = correctAnswer.toString()
            holder.totalNumberTv.text = "/${totalAnswer}题"
        }

        override fun getItemCount(): Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            // 报告总览
            0 -> {
                holder.reportDescriptionRv.layoutManager =
                    GridLayoutManager(holder.itemView.context, 1)
                holder.reportDescriptionRv.adapter = ReportOverviewAdapter()
                val params = holder.reportDescriptionRv.layoutParams as ViewGroup.LayoutParams
                // 更改用于复用的RecyclerView的宽高
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.reportDescriptionRv.layoutParams = params
                // 隐藏掉通用的CardView
                hideContentCardView(holder)
                holder.reportContentRv.layoutManager = LinearLayoutManager(holder.itemView.context)
                holder.reportContentRv.adapter = OverviewReportContentAdapter()
            }

            // 答题卡
            1 -> {
                holder.reportTypeIv.setImageResource(R.drawable.answer_sheet_finish)
                holder.reportTypeTv.text = "答题卡"
                holder.reportContentRv.layoutManager = GridLayoutManager(holder.itemView.context, 5)
                holder.reportContentRv.adapter = object : AnswerSheetAdapter(userAnswerLists) {
                    override fun getItemCount(): Int = userAnswerLists.size

                    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                        holder.button.isCheckable = false
                        holder.button.text = (position + 1).toString()
                        if (questionDataList[position].answers.toSet() == userAnswerLists[position].toSet()) {
                            // 获取ColorStateList资源
                            val colorStateList = ContextCompat.getColorStateList(
                                holder.itemView.context,
                                R.color.button_correct_background_tint_report
                            )

                            // 设置背景色调
                            ViewCompat.setBackgroundTintList(holder.button, colorStateList)
                            holder.button.setTextColor(
                                ContextCompat.getColor(
                                    holder.itemView.context,
                                    R.color.button_correct_text_color_report
                                )
                            )
                            holder.button.setStrokeColorResource(R.color.button_correct_stroke_report)
                        } else if (userAnswerLists[position].isNotEmpty()) {
                            // 获取ColorStateList资源
                            val colorStateList = ContextCompat.getColorStateList(
                                holder.itemView.context,
                                R.color.button_error_background_tint_report
                            )

                            // 设置背景色调
                            ViewCompat.setBackgroundTintList(holder.button, colorStateList)
                            holder.button.setTextColor(
                                ContextCompat.getColor(
                                    holder.itemView.context,
                                    R.color.button_error_text_color_report
                                )
                            )
                            holder.button.setStrokeColorResource(R.color.button_error_stroke_report)
                        }
                    }
                }
                // 隐藏掉通用的CardView
                hideContentCardView(holder)
            }

            // 考试情况
            2 -> {
                holder.reportTypeIv.setImageResource(R.drawable.pen)
                holder.reportTypeTv.text = "考试情况"
                holder.reportContentRv.adapter =
                    ExamStatusAdapter(totalAnswer, correctAnswer, wrongAnswer)
                holder.reportContentRv.layoutManager = GridLayoutManager(
                    holder.itemView.context, if (correctAnswer + wrongAnswer == totalAnswer) {
                        4
                    } else {
                        5
                    }
                )
                holder.reportContentRv.addItemDecoration(
                    DividerItemDecoration(
                        holder.itemView.context,
                        DividerItemDecoration.HORIZONTAL
                    )
                )
            }
        }
    }

    private fun hideContentCardView(holder: ViewHolder) {
        holder.reportContentCv.strokeWidth = 0
        holder.reportContentCv.radius = 0f
        holder.reportContentRv.setBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.recycler_view_background_report
            )
        )
    }
}