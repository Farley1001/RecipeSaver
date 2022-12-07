package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class AddCategoryUseCase(
    private val repository: RecipeRepository
) {
    @Throws(InvalidRecipeException::class)
    suspend operator fun invoke(category: Category) {
        if(category.name.isBlank()) {
            throw InvalidRecipeException("The Category name can't be empty.")
        }
        repository.insertCategory(category)
    }
}