package com.example.fenbi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fenbi.databinding.FragmentBlankBinding

class BlankFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null
    // 只能在onCreateView与onDestroyView之间的生命周期里使用
    private val binding: FragmentBlankBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}