package com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class GetIngredientByIdUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id:Long): Ingredient? {
        return repository.getIngredientById(id)
    }
}