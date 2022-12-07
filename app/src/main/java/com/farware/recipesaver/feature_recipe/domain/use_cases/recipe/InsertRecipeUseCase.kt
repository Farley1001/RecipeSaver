package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidConversionException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidConversionException::class)
    suspend operator fun invoke(recipe: Recipe) {
        if (recipe.name.isEmpty()) {
            throw InvalidRecipeException("The recipe must have a name.")
        }
        if (recipe.categoryId < 1) {
            throw InvalidRecipeException("The recipe must have a category.")
        }
        if (recipe.description.isEmpty()) {
            throw InvalidRecipeException("The recipe must have a description.")
        }
        repository.insertRecipe(recipe)
    }
}