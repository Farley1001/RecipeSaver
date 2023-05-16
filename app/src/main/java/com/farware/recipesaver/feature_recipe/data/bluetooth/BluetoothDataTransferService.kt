package com.farware.recipesaver.feature_recipe.data.bluetooth

import android.bluetooth.BluetoothSocket
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothMessage
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothTransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {

    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow<BluetoothMessage> {
            if(!socket.isConnected){
                return@flow
            }

            val buffer = ByteArray(1024)   // changed from 1024 to 2048

            while(true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw BluetoothTransferFailedException()
                }

                emit(
                    buffer.decodeToString(
                        endIndex = byteCount
                    ).toBluetoothMessage(
                        isFromLocalUser = false
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch(e: IOException) {
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}