package com.example.fenbi.adapter

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.databinding.ItemAnswerSheetBinding
import com.example.fenbi.utils.PracticeUtils
import com.google.android.material.button.MaterialButton
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


class AnswerSheetAdapter(
    private var userAnswerLists: List<ArrayList<Int>>,
    private val practiceUtils: PracticeUtils
) : RecyclerView.Adapter<AnswerSheetAdapter.ViewHolder>() {
    var answerSheetObserver: Observer<Int>? = null

    inner class ViewHolder(binding: ItemAnswerSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val button: MaterialButton = binding.answerSheetBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAnswerSheetBinding =
            ItemAnswerSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        answerSheetObserver = object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                Log.i("AnswerSheetAdapterObserver", "onSubscribe$d")
            }

            override fun onError(e: Throwable) {
                Log.e("AnswerSheetAdapterObserver", "onError:$e")
            }

            override fun onComplete() {
                Log.i("AnswerSheetAdapterObserver", "onComplete")
            }

            override fun onNext(position: Int) {
                notifyItemChanged(position)
                Log.i("AnswerSheetAdapterObserver", "onNext: $position")
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int = userAnswerLists.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == userAnswerLists.size) {
            holder.button.text = "提交"
            val params = holder.button.layoutParams as LayoutParams
            // 更改提交按钮的宽度
            params.width = LayoutParams.MATCH_PARENT
            holder.button.layoutParams = params

            // 始终为按钮而非checkbox的样式
            holder.button.isChecked = true
            holder.button.setOnClickListener {
                holder.button.isChecked = true
            }
        } else {
            holder.button.text = (position + 1).toString()
            holder.button.isChecked = userAnswerLists[position].isNotEmpty()
            // 更改提交按钮的宽度为50dp
            val params = holder.button.layoutParams as LayoutParams
            params.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, holder.button.resources.displayMetrics).toInt()
            holder.button.layoutParams = params
            holder.button.setOnClickListener {
                holder.button.isChecked = userAnswerLists[position].isNotEmpty()
                practiceUtils.goToPage(position)
            }
        }
    }
}