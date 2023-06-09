package com.farware.recipesaver.feature_recipe.presentation.share_recipe

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.farware.recipesaver.RecipeApp
import com.farware.recipesaver.feature_recipe.presentation.share_recipe.components.BluetoothDeviceContent
import com.farware.recipesaver.feature_recipe.presentation.share_recipe.components.ShareRecipeReceiveContent
import com.farware.recipesaver.feature_recipe.presentation.share_recipe.components.ShareRecipeSendContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ShareRecipeScreen(
    viewModel: ShareRecipeViewModel = hiltViewModel(),
) {
    val state: ShareRecipeUiState by viewModel.state.collectAsStateWithLifecycle()
    val bluetoothManager: BluetoothManager? = getSystemService(RecipeApp.applicationContext(), BluetoothManager::class.java)
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    val isBluetoothEnabled  = remember { mutableStateOf(bluetoothAdapter?.isEnabled == true) }

    val enableDiscoveryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != 0)
            viewModel.startScan()
    }

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            isBluetoothEnabled.value = true
        }
    )

    fun enableDiscovery() {
        CoroutineScope(Dispatchers.IO).launch {
            enableDiscoveryLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
        }
    }

    if (!isBluetoothEnabled.value) {
        LaunchedEffect(Unit) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }

    LaunchedEffect(key1 = state.errorMessage ) {
        state.errorMessage?.let { message ->
            Toast.makeText(
                RecipeApp.applicationContext(),
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = state.isConnected ) {
        if(state.isConnected) {
            Toast.makeText(
                RecipeApp.applicationContext(),
                "The app is connected",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            state.isConnecting -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(text = "Connecting...")
                }
            }
            state.isConnected -> {
                if(state.messages.isNotEmpty() && !state.messages.last().isFromLocalUser) {
                    ShareRecipeReceiveContent(
                        state = state,
                        onDisconnect = { viewModel.disconnectFromDevice() },
                        onSendMessage = { viewModel.sendMessage(it) }
                    )
                } else {
                    ShareRecipeSendContent(
                        state = state,
                        onDisconnect = { viewModel.disconnectFromDevice() },
                        onSendMessage = { viewModel.sendMessage(it) }
                    )
                }
            }
            else -> {
                BluetoothDeviceContent(
                    hasRecipe = "${state.sendString}",
                    deviceName = state.deviceName,
                    pairedDevices = state.allDevices,
                    scannedDevices = state.scannedDevices,
                    onStartScan = { viewModel.startScan() },
                    onStopScan = { viewModel.stopScan() },
                    onStartServer = { viewModel.waitForIncomingConnections() },
                    onDeviceClicked = { viewModel.connectToDevice(it) },
                    onDiscoverClicked = { enableDiscovery() },
                    onBackClicked = { viewModel.onEvent(ShareRecipeEvent.Navigate(it)) }
                )
            }
        }
    }
}