package com.farware.recipesaver.feature_recipe.domain.repository

import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    /*
            recipes
    */
    fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategory>>

    suspend fun getRecipeById(id: Long): RecipeWithCategory?

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun insertRecipeReturnId(recipe: Recipe): Long

    suspend fun deleteRecipe(recipe: Recipe)

    fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>>

    fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>>

    fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>>

    fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>>

    /*
            categories
    */
    fun getCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: Long): Category?

    suspend fun insertCategory(category: Category)

    suspend fun insertCategoryReturnId(category: Category): Long

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
    fun getMeasures(): Flow<List<Measure>>

    suspend fun getMeasureById(id: Int): Measure?

    suspend fun insertMeasure(measure: Measure)

    suspend fun insertMeasureReturnId(measure: Measure): Long

    suspend fun insertAllMeasures(measures: List<Measure>)

    /*
            ingredient
    */
    fun getIngredients(): Flow<List<Ingredient>>

    suspend fun getIngredientById(id: Long): Ingredient?

    suspend fun getIngredientByName(name: String): Ingredient?

    suspend fun insertIngredient(ingredient: Ingredient)

    suspend fun insertIngredientReturnId(ingredient: Ingredient): Long

    suspend fun deleteIngredient(ingredient: Ingredient)

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
