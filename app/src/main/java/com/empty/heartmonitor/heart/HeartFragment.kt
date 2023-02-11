package com.empty.heartmonitor.heart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.empty.heartmonitor.databinding.FragmentHeartBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeartFragment : Fragment() {

    private var _binding: FragmentHeartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: HeartViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe()
        binding.masureBt.setOnClickListener {
            viewModel.startMeasure()
        }
    }

    fun HeartViewModel.observe() {
        heartBpm.onEach {
            binding.heartRateTv.text = it.toString()
        }.launchIn(lifecycleScope)
        measuredBpm.onEach {
            binding.masuredTv.text = it.toString()
        }.launchIn(lifecycleScope)
        isMeasuring.onEach {
            binding.measureGroup.isVisible = it
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}