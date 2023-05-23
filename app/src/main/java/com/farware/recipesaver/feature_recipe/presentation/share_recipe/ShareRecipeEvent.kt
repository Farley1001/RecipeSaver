package com.farware.recipesaver.feature_recipe.presentation.share_recipe

sealed class ShareRecipeEvent {
    data class Navigate(val path: String): ShareRecipeEvent()
}