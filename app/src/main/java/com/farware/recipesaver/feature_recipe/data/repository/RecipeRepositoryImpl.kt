package com.farware.recipesaver.feature_recipe.data.repository

import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDao
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl(
    private val dao: RecipeDao
): RecipeRepository {

    /*
            recipes
    */
    override fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.getRecipes(onlyFavorites)
    }

    override suspend fun getRecipeById(id: Long): RecipeWithCategoryAndColor? {
        return dao.getRecipeById(id)
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        dao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(recipe)
    }

    override fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnName(search, onlyFavorites)!!
    }

    override fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnCategory(search, onlyFavorites)!!
    }

    override fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnIngredients(search, onlyFavorites)!!
    }

    override fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnDirections(search, onlyFavorites)!!
    }

    /*
            categories
    */
    override fun getCategories(): Flow<List<CategoryWithColor>> {
        return dao.getCategories()
    }

    override suspend fun getCategoryById(id: Int): CategoryWithColor? {
        return dao.getCategoryById(id)
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }

    /*
            steps
    */
    override fun getStepsByRecipeId(id: Long): Flow<List<Step?>> {
        return dao.getStepsByRecipeId(id)
    }

    override suspend fun insertStep(step: Step) {
        dao.insertStep(step)
    }

    override suspend fun deleteStep(step: Step) {
        dao.deleteStep(step)
    }

    /*
        tips
*/
    override fun getTipsByRecipeId(id: Long): Flow<List<Tip?>> {
        return dao.getTipsByRecipeId(id)
    }

    override suspend fun insertTip(tip: Tip) {
        dao.insertTip(tip)
    }

    override suspend fun deleteTip(tip: Tip) {
        dao.deleteTip(tip)
    }

    /*
           measure
    */
    override suspend fun insertMeasure(measure: Measure) {
        // measure come from a master on firebase
        dao.insertMeasure(measure)
    }

    override suspend fun insertAllMeasures(measures: List<Measure>) {
        dao.deleteAllMeasures()
        dao.insertAllMeasures(measures)
    }

    /*
            ingredients  (contains only ingredients used in a local recipe)
    */
    override suspend fun insertLocalIngredient(ingredient: Ingredient) {
        dao.insertIngredient(ingredient)
    }

    override suspend fun deleteLocalIngredient(ingredient: Ingredient) {
        dao.deleteIngredient(ingredient)
    }

    /*
            recipe ingredient
    */
    override fun getRecipeIngredientsByRecipeId(recipeId: Long): Flow<List<FullRecipeIngredient?>> {
        return dao.getRecipeIngredientsByRecipeId(recipeId)
    }

    override suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredient) {
        dao.insertRecipeIngredient(recipeIngredient)
    }

    override suspend fun deleteRecipeIngredient(recipeIngredient: RecipeIngredient) {
        dao.deleteRecipeIngredient(recipeIngredient)
    }

    /*
           conversion
    */
    override suspend fun insertConversion(conversion: Conversion) {
        // conversion come from a master on firebase
        dao.insertConversion(conversion)
    }

    override suspend fun insertAllConversions(conversions: List<Conversion>) {
        dao.deleteAllConversions()
        dao.insertAllConversions(conversions)
    }
}