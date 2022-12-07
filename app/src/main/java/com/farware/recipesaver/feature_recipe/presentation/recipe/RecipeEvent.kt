package com.farware.recipesaver.feature_recipe.presentation.recipe

sealed class RecipeEvent {
    data class SelectedTabChanged(val selectedIndex: Int): RecipeEvent()
    object ToggleFavorite: RecipeEvent()
    object ToggleCategoryDialog: RecipeEvent()
    object SaveNewCategory: RecipeEvent()
    object ChangeRecipeName: RecipeEvent()
    data class NewSelectedCategory(val categoryId: Long ): RecipeEvent()
    data class SavePrepTime(val prepTime: Long): RecipeEvent()
    data class SaveCookTime(val cookTime: Long): RecipeEvent()
}
