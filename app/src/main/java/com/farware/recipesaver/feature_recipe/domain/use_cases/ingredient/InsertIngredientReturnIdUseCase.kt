package com.farware.recipesaver.feature_recipe.domain.use_cases.ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidIngredientException
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertIngredientReturnIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidIngredientException::class)
    suspend operator fun invoke(ingredient: Ingredient): Long {
        if (ingredient.name.isEmpty()) {
            throw InvalidIngredientException("The ingredient must have a name.")
        }
        return repository.insertIngredientReturnId(ingredient)
    }
}