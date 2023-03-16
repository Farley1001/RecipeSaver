package com.farware.recipesaver.feature_recipe.presentation.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    object Success: UiEvent()
}
