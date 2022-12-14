package com.empty.heartmonitor

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ReadRequest
import no.nordicsemi.android.ble.ValueChangedCallback
import no.nordicsemi.android.ble.WriteRequest
import java.util.*

internal class MyBleManager(context: Context) : BleManager(context) {
    private var fluxCapacitorControlPoint: BluetoothGattCharacteristic? = null
    override fun getMinLogPriority(): Int {
        // Use to return minimal desired logging priority.
        return Log.VERBOSE
    }

    override fun log(priority: Int, message: String) {
        // Log from here.
        Log.println(priority, TAG, message)
    }

    override fun getGattCallback(): BleManagerGattCallback {
        return MyGattCallbackImpl()
    }

    private inner class MyGattCallbackImpl : BleManagerGattCallback() {
        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            // Here get instances of your characteristics.
            // Return false if a required service has not been discovered.
            val fluxCapacitorService =
                gatt.getService(SERVICE)
            if (fluxCapacitorService != null) {
                fluxCapacitorControlPoint =
                    fluxCapacitorService.getCharacteristic(CHARACTERISTIC)
            }
            return fluxCapacitorControlPoint != null
        }

        override fun initialize() {
            // Initialize your device.
            // This means e.g. enabling notifications, setting notification callbacks,
            // sometimes writing something to some Control Point.
            // Kotlin projects should not use suspend methods here, which require a scope.
            requestMtu(517)
                .enqueue()
            setNotificationCallback(
                BluetoothGattCharacteristic(
                  CHARACTERISTIC,
                    BluetoothGattCharacteristic.PROPERTY_READ,
                    BluetoothGattCharacteristic.PERMISSION_READ_ENCRYPTED
                )
            ).with { device, data ->
                Log.d("MyGattCallbackImpl","initialize: ${data.value}")
            }

        }

        override fun onServicesInvalidated() {
            // This method is called when the services get invalidated, i.e. when the device
            // disconnects.
            // References to characteristics should be nullified here.
            fluxCapacitorControlPoint = null
        }
    }

    public override fun readCharacteristic(characteristic: BluetoothGattCharacteristic?): ReadRequest {
        return super.readCharacteristic(characteristic)
    }

    public override fun writeCharacteristic(
        characteristic: BluetoothGattCharacteristic?,
        data: ByteArray?,
        writeType: Int
    ): WriteRequest {
        return super.writeCharacteristic(characteristic, data, writeType)
    }


    public override fun setNotificationCallback(characteristic: BluetoothGattCharacteristic?): ValueChangedCallback {
        return super.setNotificationCallback(characteristic)
    } // Here you may add some high level methods for your device:

    companion object {
        private const val TAG = "MyBleManager"
        private val SERVICE = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")
        val CHARACTERISTIC = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")
        val CHARACTERISTIC1 = UUID.fromString("0000FFE2-0000-1000-8000-00805F9B34FB")

    }
}