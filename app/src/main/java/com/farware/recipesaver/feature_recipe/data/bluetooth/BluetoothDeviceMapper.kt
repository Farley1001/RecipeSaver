package com.farware.recipesaver.feature_recipe.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}