package com.farware.recipesaver.feature_recipe.domain.bluetooth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>

    fun startDiscovery()

    fun stopDiscovery()

    fun startBluetoothServer(): Flow<BluetoothConnectionResult>

    fun connectToDevice(device: BluetoothDevice): Flow<BluetoothConnectionResult>

    suspend fun trySendMessage(message: String): BluetoothMessage?

    fun getBluetoothName(): String

    fun closeConnection()

    fun release()
}