package com.farware.recipesaver.feature_recipe.data.repository

import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDao
import com.farware.recipesaver.feature_recipe.data.entities.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepositoryImpl(
    private val dao: RecipeDao
): RecipeRepository {

    /*
            recipes
    */
    override fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategory>> {
        return dao.getRecipes(onlyFavorites).map { recipes -> recipes.map { it.toRecipeWithCategory() } }
    }

    override suspend fun getRecipeById(id: Long): RecipeWithCategory? {
        return dao.getRecipeById(id)?.toRecipeWithCategory()
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        dao.insertRecipe(RecipeEntity.from(recipe) )
    }

    override suspend fun insertRecipeReturnId(recipe: Recipe): Long {
        return dao.insertRecipeReturnId(RecipeEntity.from(recipe))
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(RecipeEntity.from(recipe))
    }

    override fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>> {
        return dao.searchRecipesOnName(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategory() } }
    }

    override fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>> {
        return dao.searchRecipesOnCategory(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategory() } }
    }

    override fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>> {
        return dao.searchRecipesOnIngredients(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategory() } }
    }

    override fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategory>> {
        return dao.searchRecipesOnDirections(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategory() } }
    }

    /*
            categories
    */
    override fun getCategories(): Flow<List<Category>> {
        return dao.getCategories().map { categories -> categories.map { it.toCategory() } }
    }

    override suspend fun getCategoryById(id: Int): Category? {
        return dao.getCategoryById(id)?.toCategory()
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(CategoryEntity.from(category))
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(CategoryEntity.from(category))
    }

    /*
            steps
    */
    override fun getStepsByRecipeId(id: Long): Flow<List<Step?>> {
        return dao.getStepsByRecipeId(id).map { steps -> steps.map { it?.toStep() } }
    }

    override suspend fun insertStep(step: Step) {
        dao.insertStep(StepEntity.from(step))
    }

    override suspend fun deleteStep(step: Step) {
        dao.deleteStep(StepEntity.from(step))
    }

    /*
        tips
*/
    override fun getTipsByRecipeId(id: Long): Flow<List<Tip?>> {
        return dao.getTipsByRecipeId(id).map { tips -> tips.map{ it?.toTip()} }
    }

    override suspend fun insertTip(tip: Tip) {
        dao.insertTip(TipEntity.from(tip))
    }

    override suspend fun deleteTip(tip: Tip) {
        dao.deleteTip(TipEntity.from(tip))
    }

    /*
           measure
    */
    override fun getMeasures(): Flow<List<Measure>> {
        return dao.getMeasures().map { measures -> measures.map { it.toMeasure() } }
    }

    override suspend fun getMeasureById(id: Int): Measure? {
        return dao.getMeasureById(id)?.toMeasure()
    }

    override suspend fun insertMeasure(measure: Measure) {
        // measure come from a master on firebase
        dao.insertMeasure(MeasureEntity.from(measure))
    }

    override suspend fun insertMeasureReturnId(measure: Measure): Long {
        return dao.insertMeasureReturnId(MeasureEntity.from(measure))
    }

    override suspend fun insertAllMeasures(measures: List<Measure>) {
        dao.deleteAllMeasures()
        dao.insertAllMeasures(measures.map { MeasureEntity.from(it) })
    }

    /*
            ingredients
    */
    override fun getIngredients(): Flow<List<Ingredient>> {
        return dao.getIngredients().map { ingredients -> ingredients.map { it.toIngredient() } }
    }

    override suspend fun getIngredientById(id: Long): Ingredient? {
        return dao.getIngredientById(id)?.toIngredient()
    }

    override suspend fun getIngredientByName(name: String): Ingredient? {
        return dao.getIngredientByName(name)?.toIngredient()
    }

    override suspend fun insertIngredient(ingredient: Ingredient) {
        dao.insertIngredient(IngredientEntity.from(ingredient))
    }

    override suspend fun insertIngredientReturnId(ingredient: Ingredient): Long {
        return dao.insertIngredientReturnId(IngredientEntity.from(ingredient))
    }

    override suspend fun deleteIngredient(ingredient: Ingredient) {
        dao.deleteIngredient(IngredientEntity.from(ingredient))
    }

    /*
            recipe ingredient
    */
    override fun getRecipeIngredientsByRecipeId(recipeId: Long): Flow<List<FullRecipeIngredient?>> {
        return dao.getRecipeIngredientsByRecipeId(recipeId).map { ingredients -> ingredients.map { it?.toFullRecipeIngredient() } }
    }

    override suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredient) {
        dao.insertRecipeIngredient(RecipeIngredientEntity.from(recipeIngredient))
    }

    override suspend fun deleteRecipeIngredient(recipeIngredient: RecipeIngredient) {
        dao.deleteRecipeIngredient(RecipeIngredientEntity.from(recipeIngredient))
    }

    /*
           conversion
    */
    override suspend fun insertConversion(conversion: Conversion) {
        // conversion come from a master on firebase
        dao.insertConversion(ConversionEntity.from(conversion))
    }

    override suspend fun insertAllConversions(conversions: List<Conversion>) {
        dao.deleteAllConversions()
        dao.insertAllConversions(conversions.map { ConversionEntity.from(it) })
    }
}