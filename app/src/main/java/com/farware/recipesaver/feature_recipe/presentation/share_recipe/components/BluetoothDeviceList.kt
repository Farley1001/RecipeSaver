package com.farware.recipesaver.feature_recipe.presentation.share_recipe.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farware.recipesaver.feature_recipe.domain.bluetooth.BluetoothDevice

@Composable
fun BluetoothDeviceList(
    pairedDevices: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit,
    onServerClick: () -> Unit,
    modifier: Modifier
) {
    val borderColor = if(isSystemInDarkTheme()) Color.DarkGray else Color(0xFFF5F5F5)
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp),
                tonalElevation = 5.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "All Devices",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = { onServerClick() },
                    ) {
                        Text(text = "Connect")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        /*
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp),
                elevation = 5.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = MaterialTheme.colors.surface
            ) {
                Text(
                    text = "Paired Devices",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        */
        items(pairedDevices) { device ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 0.dp),
                tonalElevation = 5.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = device.name ?: "(No Name)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDeviceClick(device) }
                        .padding(16.dp)
                )
            }
        }
        /*
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f).fillMaxHeight())
                Button(
                    modifier = Modifier.padding(end = 20.dp),
                    onClick = { onServerClick() },
                ) {
                    Text(text = "Connect")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 5.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = MaterialTheme.colors.surface
            ) {
                Text(
                    text = "Scanned Devices",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        items(scannedDevices) { device ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 5.dp,
                color = MaterialTheme.colors.surface
            ) {
                Text(
                    text = device.name ?: "(No Name)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDeviceClick(device) }
                        .padding(16.dp)
                )
            }
        }
        */
    }
}