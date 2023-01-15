package com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class GetIngredientByNameUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(name: String): Ingredient? {
        return repository.getIngredientByName(name)
    }
}