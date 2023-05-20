package com.farware.recipesaver.feature_recipe.presentation.permissions

data class PermissionsState(
    val permissionType: String = "",
    val successPath: String = "",
    val declinePath: String = ""
)