package com.farware.recipesaver.feature_recipe.presentation.permissions

sealed class PermissionsEvent {
    data class SelectedTabChanged(val selectedIndex: Int): PermissionsEvent()
    object NavigateToSuccessPath: PermissionsEvent()
    object NavigateToDeclinePath: PermissionsEvent()
}