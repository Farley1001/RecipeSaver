package com.farware.recipesaver.feature_recipe.domain.use_cases.recipe_ingredient

import com.farware.recipesaver.feature_recipe.domain.model.recipe.InvalidRecipeIngredientException
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository

class InsertRecipeIngredientUseCase(
    private val repository: RecipeRepository
) {
    @Throws(InvalidRecipeIngredientException::class)
    suspend operator fun invoke(recipeIngredient: RecipeIngredient) {
        if(recipeIngredient.ingredientId < 1) {
            throw InvalidRecipeIngredientException("The ingredientId was not supplied.")
        }
        //if(recipeIngredient.measureId < 1) {
        //    throw InvalidRecipeIngredientException("The measureId was not supplied.")
        //}
        if(recipeIngredient.recipeId < 1) {
            throw InvalidRecipeIngredientException("The recipeId was not supplied.")
        }
        if(recipeIngredient.amount.isBlank()){
            throw InvalidRecipeIngredientException("The amount of the ingredient must be blank.")
        }
        repository.insertRecipeIngredient(recipeIngredient)
    }
}