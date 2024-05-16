package com.example.fenbi.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fenbi.databinding.ToolbarPracticeBinding

class PracticeToolBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding: ToolbarPracticeBinding

    init {
        binding = ToolbarPracticeBinding.inflate(LayoutInflater.from(context), this, true)
        binding.closeBtn.setOnClickListener {
            Toast.makeText(context, "关闭", Toast.LENGTH_SHORT).show()
        }
    }
}