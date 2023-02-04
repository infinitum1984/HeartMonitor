package com.empty.heartmonitor.device.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.empty.heartmonitor.databinding.FragmentDeviceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeviceFragment : Fragment() {

    private var _binding: FragmentDeviceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: DeviceViewModel by viewModel()
    val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.startScanning()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        enableBluetooth()
    }

    private fun enableBluetooth() {
        val bluetoothManager: BluetoothManager? =
            requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothManager?.adapter?.let {
            if (!it.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }
    }

    private fun checkPermissions() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.startScanning()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}