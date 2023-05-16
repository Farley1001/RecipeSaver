package com.farware.recipesaver.feature_recipe.domain.bluetooth

sealed interface BluetoothConnectionResult {
    object ConnectionEstablished: BluetoothConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage): BluetoothConnectionResult
    data class Error(val message: String): BluetoothConnectionResult
}