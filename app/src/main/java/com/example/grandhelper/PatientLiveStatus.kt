package com.example.grandhelper

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import android.bluetooth.*
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat


@Suppress("SameParameterValue", "DEPRECATION")
@SuppressLint("SetTextI18n", "SourceLockedOrientationActivity", "MissingPermission")
class PatientLiveStatus : ComponentActivity() {
    private var pulse = 0
    //private var systolicPressure = 110
    //private var diastolicPressure = 70

    private lateinit var tvPulse: TextView
    //private lateinit var tvBloodPressure1: TextView
    //private lateinit var tvBloodPressure2: TextView

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothGatt: BluetoothGatt? = null
    private val bandMacAddress = "E2:7B:53:2C:91:4E"    // Mac address of smart band (change with smart band)
    private val REQUEST_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.patient_live_status)

        tvPulse = findViewById(R.id.pulse)
        //val tvBloodPressure1: TextView = findViewById(R.id.blood_pressure_1)
        //val tvBloodPressure2: TextView = findViewById(R.id.blood_pressure_2)

        //tvPulse.text = "$pulse bpm"
        //tvBloodPressure1.text = "$systolicPressure systolic mm Hg"
        //tvBloodPressure2.text = "$diastolicPressure diastolic mm Hg"

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }

        setupBluetooth()
    }

    private fun setupBluetooth() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter.isEnabled) { // Check for permissions before connecting
            if (checkBluetoothPermissions()) {
                connectToDevice(bandMacAddress)
            } else {
                requestPermissions()
            }
        } else {
            Log.e("BLE", "Bluetooth is disabled!")
        }
    }

    private fun connectToDevice(macAddress: String) {
        val device = bluetoothAdapter.getRemoteDevice(macAddress)
        if (device == null) {
            Log.e("BLE", "Device not found with MAC address: $macAddress")
            return
        }

        bluetoothGatt = device.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                Log.d("BLE", "Connection status: $status, newState: $newState")
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("BLE", "Successfully connected")
                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("BLE", "Disconnected from device")
                    bluetoothGatt?.close()
                    bluetoothGatt = null
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS && gatt != null) {
                    val heartRateService = gatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"))     // Address of service (should be the same)
                    val heartRateCharacteristic = heartRateService?.getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"))
                    heartRateCharacteristic?.let {
                        gatt.setCharacteristicNotification(it, true)
                        // Enable notifications on the characteristic
                        val descriptor = it.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                        descriptor?.let { desc ->
                            desc.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            gatt.writeDescriptor(desc)
                        } ?: Log.e("BLE", "Descriptor is null")
                    } ?: Log.e("BLE", "Heart Rate Characteristic is null")
                } else {
                    Log.e("BLE", "onServicesDiscovered received: $status")
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
                characteristic?.let {
                    val value: ByteArray = it.value
                    // Ensure that the heart rate value is long enough
                    if (value.isNotEmpty()) {
                        val heartRate = value[1].toInt()
                        pulse = heartRate
                        runOnUiThread {
                            tvPulse.text = "$pulse bpm"
                        }
                    } else {
                        Log.e("BLE", "Heart rate value is too short")
                    }
                } ?: Log.e("BLE", "Characteristic value is null")
            }
        })
    }

    // Check if the app has necessary Bluetooth permissions
    private fun checkBluetoothPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12 or higher (BLUETOOTH_CONNECT)
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // Request necessary Bluetooth permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_PERMISSION_CODE )
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            bluetoothGatt?.close()
        } catch (e: SecurityException) {
            Log.e("BLE", "Error closing BluetoothGatt: ${e.message}")
        }
    }
}