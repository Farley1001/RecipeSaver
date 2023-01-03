package com.farware.recipesaver.feature_recipe.data.repository

import com.farware.recipesaver.feature_recipe.data.data_source.RecipeDao
import com.farware.recipesaver.feature_recipe.data.entities.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import com.farware.recipesaver.feature_recipe.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepositoryImpl(
    private val dao: RecipeDao
): RecipeRepository {

    /*
            recipes
    */
    override fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.getRecipes(onlyFavorites).map { recipes -> recipes.map { it.toRecipeWithCategoryAndColor() } }
    }

    override suspend fun getRecipeById(id: Long): RecipeWithCategoryAndColor? {
        return dao.getRecipeById(id)?.toRecipeWithCategoryAndColor()
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        dao.insertRecipe(RecipeEntity.from(recipe) )
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(RecipeEntity.from(recipe))
    }

    override fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnName(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategoryAndColor() } }
    }

    override fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnCategory(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategoryAndColor() } }
    }

    override fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnIngredients(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategoryAndColor() } }
    }

    override fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>> {
        return dao.searchRecipesOnDirections(search, onlyFavorites)!!.map { recipes -> recipes.map { it.toRecipeWithCategoryAndColor() } }
    }

    /*
            categories
    */
    override fun getCategories(): Flow<List<CategoryWithColor>> {
        return dao.getCategories().map { categories -> categories.map { it.toCategoryWithColor() } }
    }

    override suspend fun getCategoryById(id: Int): CategoryWithColor? {
        return dao.getCategoryById(id)?.toCategoryWithColor()
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
    override suspend fun insertMeasure(measure: Measure) {
        // measure come from a master on firebase
        dao.insertMeasure(MeasureEntity.from(measure))
    }

    override suspend fun insertAllMeasures(measures: List<Measure>) {
        dao.deleteAllMeasures()
        dao.insertAllMeasures(measures.map { MeasureEntity.from(it) })
    }

    /*
            ingredients  (contains only ingredients used in a local recipe)
    */
    override suspend fun insertLocalIngredient(ingredient: Ingredient) {
        dao.insertIngredient(IngredientEntity.from(ingredient))
    }

    override suspend fun deleteLocalIngredient(ingredient: Ingredient) {
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