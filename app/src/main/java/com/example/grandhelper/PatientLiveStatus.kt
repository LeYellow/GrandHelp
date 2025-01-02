package com.example.grandhelper

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.bluetooth.*
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import android.Manifest

@Suppress("SameParameterValue")
class PatientLiveStatus : ComponentActivity() {

    private var pulse = 0
    //private var systolicPressure = 110

    private lateinit var tvPulse: TextView
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothGatt: BluetoothGatt? = null

    private val bandMacAddress = "E2:7B:53:2C:91:4E"
    private val REQUEST_PERMISSION_CODE = 1

    @SuppressLint("SetTextI18n", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.patient_live_status)

        tvPulse = findViewById(R.id.pulse)
        val tvBloodPressure1: TextView = findViewById(R.id.blood_pressure_1)

        tvBloodPressure1.text = "not on current hw"

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }

        setupBluetooth()
    }

    private fun setupBluetooth() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter.isEnabled) {
            // Check for permissions before connecting
            if (checkBluetoothPermissions()) {
                connectToDevice(bandMacAddress)
            } else {
                // Request permissions if not already granted
                requestPermissions()
            }
        } else {
            Log.e("BLE", "Bluetooth is disabled!")
        }
    }


    @SuppressLint("MissingPermission")
    private fun connectToDevice(macAddress: String) {
        val device = bluetoothAdapter.getRemoteDevice(macAddress)
        if (device == null) {
            Log.e("BLE", "Device not found with MAC address: $macAddress")
            return
        }

        bluetoothGatt = device.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BLE", "Connected to device")
                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BLE", "Disconnected from device")
                    bluetoothGatt?.close()
                    bluetoothGatt = null
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS && gatt != null) {
                    val heartRateService = gatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"))
                    val heartRateCharacteristic = heartRateService?.getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"))
                    heartRateCharacteristic?.let {
                        gatt.setCharacteristicNotification(it, true)
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            @Deprecated("Deprecated in Java")
            override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
                characteristic?.value?.let { value ->
                    if (value.size > 1) {
                        val heartRate = value[1].toInt()
                        pulse = heartRate
                        runOnUiThread {
                            // Update the UI only when the pulse value changes
                            tvPulse.text = "$pulse bpm"
                            Log.d("BLE", "Updating pulse to: $pulse bpm")
                        }
                    } else {
                        Log.e("BLE", "Heart rate value is too short")
                    }
                } ?: Log.e("BLE", "Characteristic value is null")
            }
        })
    }

    private fun checkBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, proceed with Bluetooth setup
                setupBluetooth()
            } else {
                Log.e("BLE", "Permissions denied!")
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                ),
                REQUEST_PERMISSION_CODE
            )
        } else {
            // For older Android versions (ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onDestroy() {  //cleans up after leaving page
        super.onDestroy()
        try {
            bluetoothGatt?.close()
        } catch (e: SecurityException) {
            Log.e("BLE", "Error closing BluetoothGatt: ${e.message}")
        }
    }
}