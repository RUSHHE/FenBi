package com.example.fenbi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.adapter.ReportAdapter
import com.example.fenbi.dataClass.Question
import com.example.fenbi.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private var questionDataList: ArrayList<Question>? = PracticeSingleton.questionDataList
    private var userAnswerLists: List<ArrayList<Int>>? = PracticeSingleton.userAnswerLists
    private lateinit var practiceType: String
    private var totalTime: Long = 0
    private lateinit var submitTime: String
    companion object {
        fun actionStart(context: Context, practiceType: String, totalTime: Long, submitTime: String) {
            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("practiceType", practiceType)
            intent.putExtra("totalTime", totalTime)
            intent.putExtra("submitTime", submitTime)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        practiceType = intent.getStringExtra("practiceType")!!
        totalTime = intent.getLongExtra("totalTime", 0)
        submitTime = intent.getStringExtra("submitTime")!!

        setContentView(binding.root)

        val reportAdapter = ReportAdapter(
            questionDataList = questionDataList!!,
            userAnswerLists = userAnswerLists!!,
            practiceType = practiceType,
            totalTime = totalTime.toString(),
            submitTime = submitTime
        )
        binding.reportRv.layoutManager = LinearLayoutManager(this)
        binding.reportRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: android.graphics.Rect,
                view: android.view.View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 10
                outRect.bottom = 10
                outRect.left = 20
                outRect.right = 20
            }
        })
        binding.reportRv.adapter = reportAdapter
    }
}