package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe

import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import javax.inject.Inject

class InsertSharedRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    @Throws(InvalidRecipeException::class)
    suspend operator fun invoke(category: Category, recipe: Recipe, steps: List<Step>, tips: List<Tip>, measures: List<Measure>, ingredients: List<Ingredient>, recipeIngredients: List<RecipeIngredient>) {
        // TODO: add some form of validation?
        /*
        if (recipe.name.isEmpty()) {
            throw InvalidRecipeException("The recipe must have a name.")
        }
        if (recipe.description.isEmpty()) {
            throw InvalidRecipeException("The recipe must have a description.")
        }
        */
        repository.insertSharedRecipe(category, recipe, steps, tips, measures, ingredients, recipeIngredients)
    }
}