package com.example.fenbi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fenbi.databinding.ItemBankChooseBinding
import com.google.android.material.textview.MaterialTextView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

class ChooseBankAdapter : RecyclerView.Adapter<ChooseBankAdapter.ViewHolder>() {
    var chooseBankObserver: Observer<Int>? = null

    inner class ViewHolder(binding: ItemBankChooseBinding) : RecyclerView.ViewHolder(binding.root) {
        val bankContainer: ConstraintLayout = binding.bankContainer
        val bankName: MaterialTextView = binding.bankNameTv
        val bankTotal: MaterialTextView = binding.bankTotalTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBankChooseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bankContainer.setOnClickListener {
            val chooseBankObservable: Observable<Int> = Observable.just(position)
            if (chooseBankObserver != null) {
                chooseBankObservable.subscribe(chooseBankObserver!!)
            }
        }
        when (position) {
            0 -> {
                holder.bankName.text = "习近平新时代中国特色社会主义思想概论"
                holder.bankTotal.text = "题"
            }

            1 -> {
                holder.bankName.text = "马克思主义基本原理"
                holder.bankTotal.text = "题"
            }

            2 -> {
                holder.bankName.text = "毛泽东思想和中国特色社会主义理论体系概论"
                holder.bankTotal.text = "题"
            }

            3 -> {
                holder.bankName.text = "中国近代史纲要"
                holder.bankTotal.text = "题"
            }

            4 -> {
                holder.bankName.text = "思想道德与法治"
                holder.bankTotal.text = "题"
            }
        }
    }
}