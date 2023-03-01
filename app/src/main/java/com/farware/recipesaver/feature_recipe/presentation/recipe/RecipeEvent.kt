package com.farware.recipesaver.feature_recipe.presentation.recipe

sealed class RecipeEvent {
    data class SelectedTabChanged(val selectedIndex: Int): com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    object ToggleFavorite: com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    object ToggleCategoryDialog: com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    object SaveNewCategory: com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    object ChangeRecipeName: com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    data class NewSelectedCategory(val categoryId: Long ): com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    data class SavePrepTime(val prepTime: Long): com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
    data class SaveCookTime(val cookTime: Long): com.farware.recipesaver.feature_recipe.presentation.recipe.RecipeEvent()
}
