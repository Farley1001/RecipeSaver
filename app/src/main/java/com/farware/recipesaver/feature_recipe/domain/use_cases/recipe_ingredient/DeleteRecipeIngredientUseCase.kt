package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class DeleteRecipeIngredientUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipeIngredient: RecipeIngredient) {
        repository.deleteRecipeIngredient(recipeIngredient)
    }
}