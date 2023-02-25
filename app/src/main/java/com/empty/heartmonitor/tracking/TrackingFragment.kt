package com.empty.heartmonitor.tracking

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.empty.heartmonitor.core.ext.isServiceRunning
import com.empty.heartmonitor.databinding.FragmentSettingsBinding
import com.empty.heartmonitor.service.TrackingService

class TrackingFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val trackingViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireContext().isServiceRunning(TrackingService::class.java)) {
            binding.button.text = "Зупинити відстежування"
            binding.button.text = "0"
        } else {
            binding.button.text = "1"
            binding.button.text = "Запустити відстежування"
        }
        binding.button.setOnClickListener {
            when (binding.button.tag) {
                "0" -> requireContext().stopService(
                    Intent(
                        requireContext(),
                        TrackingService::class.java
                    )
                )
                "1" -> requireContext().startForegroundService(
                    Intent(
                        requireContext(),
                        TrackingService::class.java
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}