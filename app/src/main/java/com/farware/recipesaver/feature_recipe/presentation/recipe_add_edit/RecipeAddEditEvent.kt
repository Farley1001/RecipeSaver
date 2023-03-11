package com.farware.recipesaver.feature_recipe.presentation.recipe_add_edit

sealed class RecipeAddEditEvent {
    data class NewCategorySelected(val categoryId: Long ): RecipeAddEditEvent()
    data class RecipeNameTextChange(val name: String): RecipeAddEditEvent()
    data class RecipeDescriptionTextChange(val description: String): RecipeAddEditEvent()
    object SaveRecipe: RecipeAddEditEvent()
    object CancelRecipe: RecipeAddEditEvent()
}