package com.empty.heartmonitor

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.icu.lang.UCharacter.JoiningGroup.PE
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.empty.heartmonitor.databinding.FragmentBleManagerBinding
import kotlinx.coroutines.withContext
import no.nordicsemi.android.ble.PhyRequest


class BleManagerFragment : Fragment() {

    private var _binding: FragmentBleManagerBinding? = null
    private val binding get() = _binding!!
    private val listDevices = arrayListOf<BluetoothDevice>()

    private var bluetoothService: BluetoothLeService? = null
    lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bleManager: MyBleManager

    private val listDevicesAdapter = DevicesAdapter({ device ->
        Log.d("TestFragment", ": connect click ${device.name}")

        bleManager.connect(device) // Automatic retries are supported, in case of 133 error.
            .retry(
                3 /* times, with */,
                100 /* ms interval */
            ) // A connection timeout can be set. This is additional to the Android's connection timeout which is 30 seconds.
            .timeout(15000 /* ms */) // The auto connect feature from connectGatt is available as well
            .useAutoConnect(true) // This API can be set on any Android version, but will only be used on devices running Android 8+ with
            // support to the selected PHY.
            .usePreferredPhy(PhyRequest.PHY_LE_1M_MASK or PhyRequest.PHY_LE_2M_MASK or PhyRequest.PHY_LE_CODED_MASK) // A connection timeout can be also set. This is additional to the Android's connection timeout which is 30 seconds.
            .timeout(15000 /* ms */) // Each request has number of callbacks called in different situations:
            .before { device -> }
            .done { device -> Log.d("BleManagerFragment", ": done ")
                requireActivity().runOnUiThread {
                    binding.tvCurrentDevice.text = "Device: ${device}"
                }
            }
            .fail { device, status -> Log.d("BleManagerFragment", ": fail ${status} ") }
            .then { device ->



            } // Each request must be enqueued.
            // Kotlin projects can use suspend() or suspendForResult() instead.
            // Java projects can also use await() which is blocking.
            .enqueue()



    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBleManagerBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDevicesLit.adapter = listDevicesAdapter


        val bluetoothManager: BluetoothManager? =
            requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.getAdapter()!!
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }
        scanLeDevice(bluetoothAdapter!!.bluetoothLeScanner)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
        bleManager = MyBleManager(requireContext()){
            requireActivity().runOnUiThread {
                binding.tvData.text = it.toString()
            }
        }
        binding.btRead.setOnClickListener {
            bleManager.readCharacteristic(
                BluetoothGattCharacteristic(
                    MyBleManager.CHARACTERISTIC1,
                    BluetoothGattCharacteristic.PROPERTY_READ,
                    BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED_MITM
                )
            ).with { device, data ->
                Log.d("BleManagerFragment", ":${data.value} ")
            }.fail { device, status ->

                Log.d("BleManagerFragment", "error : ${status}")
            }.enqueue()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var scanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    @SuppressLint("MissingPermission")
    private fun scanLeDevice(bluetoothLeScanner: BluetoothLeScanner) {
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false

                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            addDevice(result.device)
        }
    }

    private fun addDevice(device: BluetoothDevice) {
        if (listDevices.firstOrNull { it.address == device.address } == null) {
            Log.d("TestFragment", "onScanResult: ${device.name} ${device.address}")
            listDevices.add(device)
            requireActivity().runOnUiThread {
                listDevicesAdapter.setData(listDevices)
            }
        }

    }


}