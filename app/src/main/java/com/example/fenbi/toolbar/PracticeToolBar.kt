package com.example.fenbi.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fenbi.databinding.ToolbarPracticeBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

class PracticeToolBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val binding: ToolbarPracticeBinding =
        ToolbarPracticeBinding.inflate(LayoutInflater.from(context), this, true)
    var toolbarObserver: Observer<Void>? = null

    init {
        binding.closeBtn.setOnClickListener {
            Toast.makeText(context, "关闭", Toast.LENGTH_SHORT).show()
        }
        binding.answerSheetBtn.setOnClickListener {
            val toolbarObservable = Observable.empty<Void>()
            if (toolbarObserver != null) {
                toolbarObservable.subscribe(toolbarObserver!!)
            }
        }
    }
}