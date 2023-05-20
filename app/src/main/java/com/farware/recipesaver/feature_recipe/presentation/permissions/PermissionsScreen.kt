package com.farware.recipesaver.feature_recipe.presentation.permissions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.feature_recipe.presentation.openAppSettings

@Composable
fun PermissionsScreen(
    viewModel: PermissionsViewModel = hiltViewModel()
) {
    // permissions
    var permissionsToRequest = emptyArray<String>()
    if(viewModel.state.value.permissionType == PermissionsType.BLUETOOTH.toString()) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsToRequest = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
        } else {
            permissionsToRequest = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
    if(viewModel.state.value.permissionType == PermissionsType.INTERNET.toString()) {
        permissionsToRequest = arrayOf(
            Manifest.permission.INTERNET
        )
    }

    val permissionsChecked = remember { mutableStateOf(false) }
    val dismissDialog = remember { mutableStateOf(false) }
    val activity = LocalContext.current as Activity
    val dialogQueue = viewModel.visiblePermissionDialogQueue

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsChecked.value = true
            permissionsToRequest.forEach {
                    permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        multiplePermissionResultLauncher.launch(
            permissionsToRequest
        )
    }

    if(dialogQueue.isEmpty() && permissionsChecked.value) {
        viewModel.onEvent(PermissionsEvent.NavigateToSuccessPath)
    }

    if (dialogQueue.isNotEmpty()) {
        dialogQueue
            .reversed()
            .forEach { permission ->
                PermissionDialog(
                    permissionTextProvider = when (permission) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            LocationPermissionTextProvider()
                        }
                        Manifest.permission.BLUETOOTH -> {
                            BluetoothPermissionTextProvider()
                        }
                        Manifest.permission.BLUETOOTH_SCAN -> {
                            BluetoothPermissionTextProvider()
                        }
                        Manifest.permission.BLUETOOTH_CONNECT -> {
                            BluetoothPermissionTextProvider()
                        }
                        Manifest.permission.BLUETOOTH_ADVERTISE -> {
                            BluetoothPermissionTextProvider()
                        }
                        else -> return@forEach
                    },
                    isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permission
                    ),
                    onDismissClick = {
                        dismissDialog.value = true
                        viewModel.onEvent(PermissionsEvent.NavigateToDeclinePath)
                    },
                    onOkClick = {
                        viewModel.dismissDialog()
                        multiplePermissionResultLauncher.launch(
                            arrayOf(permission)
                        )
                    },
                    onGoToAppSettingsClick = {
                        activity.openAppSettings()
                    })
            }
    }

}