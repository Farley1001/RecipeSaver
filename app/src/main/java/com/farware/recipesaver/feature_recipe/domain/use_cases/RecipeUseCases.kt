package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.recipe.*

data class RecipeUseCases(
    val getRecipes: GetRecipesUseCase,
    val deleteRecipe: DeleteRecipeUseCase,
    val getRecipe: GetRecipeUseCase,
    val insertRecipe: InsertRecipeUseCase,
    val getCategories: GetCategoriesUseCase,
    val deleteCategory: DeleteCategoryUseCase,
    val insertCategory: InsertCategoryUseCase,
    val getCategory: GetCategoryUseCase,
    val searchRecipesOnCategory: SearchRecipesOnCategoryUseCase,
    val searchRecipesOnDirections: SearchRecipesOnDirectionsUseCase,
    val searchRecipesOnIngredients: SearchRecipesOnIngredientsUseCase,
    val searchRecipesOnName: SearchRecipesOnNameUseCase,
    val insertConversion: InsertConversionUseCase,
    val insertAllConversions: InsertAllConversionsUseCase,
    val insertMeasure: InsertMeasureUseCase,
    val insertAllMeasures: InsertAllMeasuresUseCase,
    val insertRecipeReturnId: InsertRecipeReturnIdUseCase,
    val insertCategoryReturnId: InsertCategoryReturnIdUseCase,
    val insertSharedRecipe: InsertSharedRecipeUseCase,
)
