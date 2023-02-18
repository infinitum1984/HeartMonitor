package com.empty.heartmonitor.temperature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.empty.heartmonitor.databinding.FragmentTemperatureBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class TemperatureFragment : Fragment() {

    private var _binding: FragmentTemperatureBinding? = null

    private val binding get() = _binding!!

    val viewModel: TemperatureViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTemperatureBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe()
        binding.masureBt.setOnClickListener {
            viewModel.startMeasure()
        }
    }

    fun TemperatureViewModel.observe() {
        temperature.onEach {
            binding.temperatureTv.text = "$it°C"
        }.launchIn(lifecycleScope)
        measuredTemperature.onEach {
            binding.masuredTv.text = it.toString()
        }.launchIn(lifecycleScope)
        isMeasuring.onEach {
            binding.measureGroup.isVisible = it
        }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}