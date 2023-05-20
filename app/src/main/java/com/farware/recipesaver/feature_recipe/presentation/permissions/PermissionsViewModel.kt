package com.farware.recipesaver.feature_recipe.presentation.permissions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.farware.recipesaver.feature_recipe.presentation.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
): ViewModel()  {

    private var _state =  mutableStateOf(PermissionsState())
    val state: State<PermissionsState> = _state

    // permissions
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    init {
        // get array of permissions needed
        savedStateHandle.get<String>("permissionType")?.let { permType ->
            if(permType != "") {
                _state.value = state.value.copy(
                    permissionType = permType
                )
            }
        }
        // get the path to navigate to on permissions success
        savedStateHandle.get<String>("successPath")?.let { path ->
            if(path != "") {
                _state.value = state.value.copy(
                    successPath = path.replace("-", "/")
                )
            }
        }
        // get the path to navigate to on permissions declined
        savedStateHandle.get<String>("declinePath")?.let { path ->
            if(path != "") {
                _state.value = state.value.copy(
                    declinePath = path.replace("-", "/")
                )
            }
        }
    }

    fun onEvent(event: PermissionsEvent) {
        when (event) {
            is PermissionsEvent.SelectedTabChanged -> {
                // TODO:
            }
            is PermissionsEvent.NavigateToSuccessPath -> {
                appNavigator.tryNavigateTo(state.value.successPath)
            }
            is PermissionsEvent.NavigateToDeclinePath -> {
                appNavigator.tryNavigateTo(state.value.declinePath)
            }
        }
    }
}