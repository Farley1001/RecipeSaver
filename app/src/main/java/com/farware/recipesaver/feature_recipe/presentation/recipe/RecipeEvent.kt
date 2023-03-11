package com.farware.recipesaver.feature_recipe.presentation.recipe

import com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit.RecipeAddEditEvent

sealed class RecipeEvent {
    data class SelectedTabChanged(val selectedIndex: Int): RecipeEvent()
    object NavigateBack: RecipeEvent()
    object ToggleFavorite: RecipeEvent()
    object ToggleCategoryDialog: RecipeEvent()
    object SaveNewCategory: RecipeEvent()
    data class RecipeNameTextChange(val name: String): RecipeEvent()
    data class RecipeDescriptionTextChange(val description: String): RecipeEvent()
    object SaveRecipe: RecipeEvent()
    object ToggleAddEditDialog: RecipeEvent()
    data class NewCategorySelected(val categoryId: Long ): RecipeEvent()
    data class SavePrepTime(val prepTime: Long): RecipeEvent()
    data class SaveCookTime(val cookTime: Long): RecipeEvent()
}
