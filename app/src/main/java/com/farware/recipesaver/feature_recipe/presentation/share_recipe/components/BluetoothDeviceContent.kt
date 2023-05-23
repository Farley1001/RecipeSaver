package com.farware.recipesaver.feature_recipe.presentation.share_recipe.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothDevice
import com.farware.recipesaver.feature_recipe.presentation.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDeviceContent(
    hasRecipe: String,
    deviceName: String,
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onStartServer: () -> Unit,
    onDeviceClicked: (BluetoothDevice) -> Unit,
    onDiscoverClicked: () -> Unit,
    onBackClicked: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exchange Recipes") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClicked(Destination.RecipesScreen())
                        }
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Localized description")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                text = "Your Device Name\n$deviceName",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(18.dp))
            BluetoothDeviceList(
                pairedDevices = pairedDevices,
                scannedDevices = scannedDevices,
                onDeviceClick = { onDeviceClicked(it) },
                onServerClick = { onStartServer() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(onClick = { onDiscoverClicked() } ) {
                    Text(text = "Discover")
                }
                Button(onClick = { onStartScan() } ) {
                    Text(text = "Start Scan")
                }
                Button(onClick = { onStopScan() } ) {
                    Text(text = "Stop Scan")
                }
            }
        }
    }
}