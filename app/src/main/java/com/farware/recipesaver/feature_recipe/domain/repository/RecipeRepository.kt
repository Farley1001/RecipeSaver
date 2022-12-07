package com.farware.recipesaver.feature_recipe.domain.repository

import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    /*
            recipes
    */
    fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    suspend fun getRecipeById(id: Long): RecipeWithCategoryAndColor?

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    /*
            categories
    */
    fun getCategories(): Flow<List<CategoryWithColor>>

    suspend fun getCategoryById(id: Int): CategoryWithColor?

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    /*
            steps
    */
    fun getStepsByRecipeId(id: Long): Flow<List<Step?>>

    suspend fun insertStep(step: Step)

    suspend fun deleteStep(step: Step)

    /*
            tips
    */
    fun getTipsByRecipeId(id: Long): Flow<List<Tip?>>

    suspend fun insertTip(tip: Tip)

    suspend fun deleteTip(tip: Tip)

    /*
            measure
    */
    suspend fun insertMeasure(measure: Measure)

    suspend fun insertAllMeasures(measures: List<Measure>)

    /*
            ingredient
    */
    suspend fun insertLocalIngredient(ingredient: Ingredient)

    suspend fun deleteLocalIngredient(ingredient: Ingredient)

    /*
            recipe ingredient
    */
    fun getRecipeIngredientsByRecipeId(recipeId: Long): Flow<List<FullRecipeIngredient?>>

    suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredient)

    suspend fun deleteRecipeIngredient(recipeIngredient: RecipeIngredient)

    /*
            conversion
    */
    suspend fun insertConversion(conversion: Conversion)

    suspend fun insertAllConversions(conversions: List<Conversion>)
}
