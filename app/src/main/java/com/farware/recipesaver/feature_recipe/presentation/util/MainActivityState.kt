package com.farware.recipesaver.feature_recipe.presentation.util

data class MainActivityState(
    val useDynamicColor: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = "",
)
