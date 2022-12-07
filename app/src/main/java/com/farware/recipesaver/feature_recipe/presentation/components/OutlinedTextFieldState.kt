package com.farware.recipesaver.feature_recipe.presentation.components

data class OutlinedTextFieldState(
    val text: String = "",
    val label: String = "",
    val isDirty: Boolean = false,
    val hasError: Boolean = false,
    val errorMsg: String = ""
)
