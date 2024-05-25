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
    var questionDataList: ArrayList<Question>? = PracticeSingleton.questionDataList
    var userAnswerLists: List<ArrayList<Int>>? = PracticeSingleton.userAnswerLists
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, ReportActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val reportAdapter = ReportAdapter(questionDataList!!, userAnswerLists!!)
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