package com.farware.recipesaver.feature_recipe.data.data_source

import androidx.room.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.*
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    /*
            recipe entity
    */
    @Transaction
    @Query("SELECT * FROM recipe where favorite = :onlyFavorites or favorite = 1")
    fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>

    @Transaction
    @Query("Select * from recipe where recipeId = :id")
    suspend fun getRecipeById(id: Long): RecipeWithCategoryAndColor?

    @Transaction
    @Query("Select * from recipe where name LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>?

    @Transaction
    @Query("Select recipe.* From recipe as recipe " +
            "Inner Join Category as category on recipe.categoryId = category.categoryId " +
            "Where category.name Like '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)   // suppress the warning not all columns used
    //@Query("Select * from recipe where ingredients LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    @Query("Select distinct * From recipe as recipe " +
            "Inner Join recipe_ingredient as recipeIngredient on recipeIngredient.recipeId = recipe.recipeId " +
            "Inner Join ingredient as ingredient on ingredient.ingredientId = recipeIngredient.ingredientId " +
            " where ingredient.name Like '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)   // suppress the warning not all columns used
    //@Query("Select * from recipe where directions LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    @Query("Select * from recipe as recipe " +
            "Inner Join Step as step on step.recipeId = recipe.recipeId " +
            "where text LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryAndColor>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    /*
            category entity
    */
    @Transaction
    @Query("Select * from category")
    fun getCategories(): Flow<List<CategoryWithColor>>

    @Transaction
    @Query("Select * from category where categoryId = :id")
    suspend fun getCategoryById(id: Int): CategoryWithColor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    /*
            category_color entity
    */
    @Query("Select * from category_color")
    fun getCategoryColors(): Flow<List<CategoryColor>>

    @Query("Select * from category_color where categoryColorId = :id")
    suspend fun getCategoryColorById(id: Int): CategoryColor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryColor(categoryColor: CategoryColor)

    @Delete
    suspend fun deleteCategoryColor(categoryColor: CategoryColor)

    /*
            step entity
    */
    @Query("Select * from step")
    fun getAllSteps(): Flow<List<Step>>

    @Query("Select * from step where stepId = :id")
    suspend fun getStepById(id: Int): Step?

    @Query("Select * from step where recipeId = :id order by stepNumber")
    fun getStepsByRecipeId(id: Long): Flow<List<Step?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: Step)

    @Delete
    suspend fun deleteStep(step: Step)

    /*
            tip entity
    */
    @Query("Select * from tip")
    fun getAllTips(): Flow<List<Tip>>

    @Query("Select * from tip where tipId = :id")
    suspend fun getTipById(id: Int): Tip?

    @Query("Select * from tip where recipeId = :id order by tipNumber")
    fun getTipsByRecipeId(id: Long): Flow<List<Tip?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: Tip)

    @Delete
    suspend fun deleteTip(tip: Tip)

    /*
            ingredient entity
    */
    @Query("Select * from ingredient")
    fun getIngredients(): Flow<List<Ingredient>>

    @Query("Select * from ingredient where ingredientId = :id")
    suspend fun getIngredientById(id: Int): Ingredient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    /*
            recipe_ingredient entity
    */
    @Transaction
    @Query("Select * from recipe_ingredient")
    fun getRecipeIngredients(): Flow<List<FullRecipeIngredient>>

    @Transaction
    @Query("Select * from recipe_ingredient where recipeId = :id")
    suspend fun getRecipeIngredientById(id: Int): FullRecipeIngredient?

    @Transaction
    @Query("Select * from recipe_ingredient where recipeId = :id")
    fun getRecipeIngredientsByRecipeId(id: Long): Flow<List<FullRecipeIngredient?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredient)

    @Delete
    suspend fun deleteRecipeIngredient(recipeIngredient: RecipeIngredient)

    /*
            measure entity
    */
    @Query("Select * from measure")
    fun getMeasures(): Flow<List<Measure>>

    @Query("Select * from measure where measureId = :id")
    suspend fun getMeasureById(id: Int): Measure?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasure(measure: Measure)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMeasures(conversions: List<Measure>)

    @Delete
    suspend fun deleteMeasure(measure: Measure)

    @Query("Delete From measure")
    suspend fun deleteAllMeasures()

    /*
            conversion entity
    */
    @Query("Select * from conversion")
    fun getConversions(): Flow<List<Conversion>>

    @Query("Select * from conversion where conversionId = :id")
    suspend fun getConversionById(id: Int): Conversion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversion(conversion: Conversion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllConversions(conversions: List<Conversion>)

    @Delete
    suspend fun deleteConversions(conversion: Conversion)

    @Query("Delete From conversion")
    suspend fun deleteAllConversions()
}