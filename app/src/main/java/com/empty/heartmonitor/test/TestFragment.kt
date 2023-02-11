package com.empty.heartmonitor.test

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.empty.heartmonitor.ble.data.BluetoothLeService
import com.empty.heartmonitor.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!
    private val listDevices = arrayListOf<BluetoothDevice>()

    private var bluetoothService: BluetoothLeService? = null
    lateinit var bluetoothAdapter: BluetoothAdapter

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            componentName: ComponentName,
            service: IBinder
        ) {
            Log.d("TestFragment", "onServiceConnected: ")
            bluetoothService = (service as BluetoothLeService.LocalBinder).getService()
            bluetoothService?.let { bluetooth ->
                if (!bluetooth.initialize()) {
                    Log.d("TestFragment", "onServiceConnected: Unable to initialize Bluetooth")
                }
                //bluetooth.connect(currentDevice)

                // call functions on service to check connection and connect to devices
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.d("TestFragment", "onServiceDisconnected: ")
            bluetoothService = null
        }
    }
    private val gattUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("TestFragment", "onReceive: ${intent.action}")
            Toast.makeText(requireContext(), "${intent.action}", Toast.LENGTH_LONG).show()
            when (intent.action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {
                    Log.d("TestFragment", "onReceive: ACTION_GATT_CONNECTED")
                }
                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {
                    Log.d("TestFragment", "onReceive: ACTION_GATT_DISCONNECTED")
                }
                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(bluetoothService?.getSupportedGattServices())
                }
            }
        }
    }

    private fun displayGattServices(gattServices: List<BluetoothGattService?>?) {

    }

    @SuppressLint("MissingPermission")
    private val listDevicesAdapter = DevicesAdapter({ device ->
        Log.d("TestFragment", ": connect click ${device.name}")
        if (bluetoothService != null) {
            val result = bluetoothService!!.connect(device.address)
            Log.d("TestFragment", ": ${result}")
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gattServiceIntent = Intent(requireContext(), BluetoothLeService::class.java)
        val binded = requireActivity().bindService(
            gattServiceIntent, serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        Log.d("TestFragment", "onCreate: ${binded}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDevicesLit.adapter = listDevicesAdapter


        val bluetoothManager: BluetoothManager? =
            requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.adapter!!
        if (bluetoothAdapter.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }
        scanLeDevice(bluetoothAdapter.bluetoothLeScanner)

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )


    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter())

    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(gattUpdateReceiver)
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter? {
        return IntentFilter().apply {
            addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
            addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
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

    @SuppressLint("MissingPermission")
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