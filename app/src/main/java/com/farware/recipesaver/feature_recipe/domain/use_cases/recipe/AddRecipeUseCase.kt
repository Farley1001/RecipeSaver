package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class AddRecipeUseCase(
    private val repository: RecipeRepository
) {
    @Throws(InvalidRecipeException::class)
    suspend operator fun invoke(recipe: Recipe) {
        if(recipe.name.isBlank()) {
            throw InvalidRecipeException("The name of the recipe can't be empty.")
        }
        repository.insertRecipe(recipe)
    }
}