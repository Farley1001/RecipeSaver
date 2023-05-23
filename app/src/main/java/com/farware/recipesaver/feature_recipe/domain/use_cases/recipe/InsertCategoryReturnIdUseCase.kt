package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidCategoryException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertCategoryReturnIdUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidCategoryException::class)
    suspend operator fun invoke(category: Category): Long {
        if (category.name.isEmpty()) {
            throw InvalidRecipeException("The category must have a name.")
        }
        if (category.darkThemeColor < 1) {
            throw InvalidRecipeException("The category must have a dark theme background color.")
        }
        if (category.onDarkThemeColor < 1) {
            throw InvalidRecipeException("The category must have a dark them text color.")
        }
        if (category.lightThemeColor < 1) {
            throw InvalidRecipeException("The category must have a light theme background color.")
        }
        if (category.onLightThemeColor < 1) {
            throw InvalidRecipeException("The category must have a light them text color.")
        }

        return repository.insertCategoryReturnId(category)
    }
}