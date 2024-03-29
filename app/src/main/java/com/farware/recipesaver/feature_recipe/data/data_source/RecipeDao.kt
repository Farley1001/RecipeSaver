package com.farware.recipesaver.feature_recipe.data.data_source

import androidx.room.*
import com.farware.recipesaver.feature_recipe.data.entities.*
import com.farware.recipesaver.feature_recipe.data.entities.relations.FullRecipeIngredientRelation
import com.farware.recipesaver.feature_recipe.data.entities.relations.RecipeWithCategoryRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    /*
            recipe entity
    */
    @Transaction
    @Query("SELECT * FROM recipe_table where favorite = :onlyFavorites or favorite = 1")
    fun getRecipes(onlyFavorites: Boolean): Flow<List<RecipeWithCategoryRelation>>

    @Transaction
    @Query("Select * from recipe_table where recipeId = :id")
    suspend fun getRecipeById(id: Long): RecipeWithCategoryRelation?

    @Transaction
    @Query("Select * from recipe_table where name LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnName(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryRelation>>?

    @Transaction
    @Query("Select recipe.* From recipe_table as recipe " +
            "Inner Join category_table as category on recipe.categoryId = category.categoryId " +
            "Where category.name Like '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnCategory(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryRelation>>?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)   // suppress the warning not all columns used
    //@Query("Select * from recipe where ingredients LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    @Query("Select distinct * From recipe_table as recipe " +
            "Inner Join recipe_ingredient_table as recipeIngredient on recipeIngredient.recipeId = recipe.recipeId " +
            "Inner Join ingredient_table as ingredient on ingredient.ingredientId = recipeIngredient.ingredientId " +
            " where ingredient.name Like '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnIngredients(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryRelation>>?

    @Transaction
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)   // suppress the warning not all columns used
    //@Query("Select * from recipe where directions LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    @Query("Select * from recipe_table as recipe " +
            "Inner Join step_table as step on step.recipeId = recipe.recipeId " +
            "where text LIKE '%' || :search || '%' and (favorite = :onlyFavorites or favorite = 1)")
    fun searchRecipesOnDirections(search: String, onlyFavorites: Boolean): Flow<List<RecipeWithCategoryRelation>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipeReturnId(recipe: RecipeEntity): Long

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Transaction
    suspend fun deleteCompleteRecipe(recipe: RecipeEntity) {
        deleteStepByRecipeId(recipe.recipeId!!)
        deleteTipByRecipeId(recipe.recipeId!!)
        deleteRecipeIngredientByRecipeId(recipe.recipeId!!)
        deleteRecipe(recipe)
    }
    /*
            insert a shared recipe
    */

    @Transaction
    suspend fun insertSharedRecipe(category: CategoryEntity, recipe: RecipeEntity, steps: List<StepEntity>, tips: List<TipEntity>, measures: List<MeasureEntity>, ingredients: List<IngredientEntity>, recipeIngredients: List<RecipeIngredientEntity>) {
        var catId = recipe.categoryId
        // create new category if needed
        if(catId < 1) {
            catId = insertCategoryReturnId(category)
            // save categoryId into recipe
            recipe.copy(categoryId = catId)
        }
        // create recipe
        var repId = insertRecipeReturnId(recipe)

        // map recipeId into steps and insert
        var stepsWithId = steps.map{
            it.copy(recipeId = repId)
        }
        insertSteps(stepsWithId)
        //stepsWithId.forEach { insertStep(it) }

        // map recipeId into tips and insert
        var tipsWithId = tips.map{
            it.copy(recipeId = repId)
        }
        //tipsWithId.forEach { insertTip(it) }


        var index: Int = 0

        // iterate each recipeIngredient
        for(index in recipeIngredients.indices) {
            var newRecIng: RecipeIngredientEntity = recipeIngredients[index]

            // insert measure and map measureId to recipeIngredient
            if(recipeIngredients[index].measureId == 0L) {
                var mesId = insertMeasureReturnId(measures[index])
                newRecIng.copy(measureId = mesId)
            }

            // insert ingredient and map ingredientId to recipeIngredient
            if(recipeIngredients[index].ingredientId == 0L) {
                var ingId = insertIngredientReturnId(ingredients[index])
                newRecIng.copy(ingredientId = ingId)
            }

            // map recipeId to recipeIngredient and insert recipeIngredient
            newRecIng.copy(recipeId = repId)
            insertRecipeIngredient(newRecIng)
        }
    }

    /*
            category entity
    */
    @Transaction
    @Query("Select * from category_table")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("Select * from category_table where categoryId = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryReturnId(category: CategoryEntity): Long

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    /*
            category_color entity
    *//*
    @Query("Select * from category_color_table")
    fun getCategoryColors(): Flow<List<CategoryColorEntity>>

    @Query("Select * from category_color_table where categoryColorId = :id")
    suspend fun getCategoryColorById(id: Int): CategoryColorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryColor(categoryColor: CategoryColorEntity)

    @Delete
    suspend fun deleteCategoryColor(categoryColor: CategoryColorEntity)*/

    /*
            step entity
    */
    @Query("Select * from step_table")
    fun getAllSteps(): Flow<List<StepEntity>>

    @Query("Select * from step_table where stepId = :id")
    suspend fun getStepById(id: Int): StepEntity?

    @Query("Select * from step_table where recipeId = :id order by stepNumber")
    fun getStepsByRecipeId(id: Long): Flow<List<StepEntity?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: StepEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    @Delete
    suspend fun deleteStep(step: StepEntity)

    @Transaction
    @Query("Delete from step_table where recipeId = :id")
    suspend fun deleteStepByRecipeId(id: Long)

    /*
            tip entity
    */
    @Query("Select * from tip_table")
    fun getAllTips(): Flow<List<TipEntity>>

    @Query("Select * from tip_table where tipId = :id")
    suspend fun getTipById(id: Int): TipEntity?

    @Query("Select * from tip_table where recipeId = :id order by tipNumber")
    fun getTipsByRecipeId(id: Long): Flow<List<TipEntity?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: TipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTips(steps: List<TipEntity>)

    @Delete
    suspend fun deleteTip(tip: TipEntity)

    @Transaction
    @Query("Delete from tip_table where recipeId = :id")
    suspend fun deleteTipByRecipeId(id: Long)

    /*
            ingredient entity
    */
    @Query("Select * from ingredient_table")
    fun getIngredients(): Flow<List<IngredientEntity>>

    @Query("Select * from ingredient_table where ingredientId = :id")
    suspend fun getIngredientById(id: Long): IngredientEntity?

    @Query("Select * from ingredient_table where name = :name")
    suspend fun getIngredientByName(name: String): IngredientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIngredientReturnId(ingredient: IngredientEntity): Long

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)

    /*
            recipe_ingredient entity
    */
    @Transaction
    @Query("Select * from recipe_ingredient_table")
    fun getRecipeIngredients(): Flow<List<FullRecipeIngredientRelation>>

    @Transaction
    @Query("Select * from recipe_ingredient_table where recipeId = :id")
    suspend fun getRecipeIngredientById(id: Int): FullRecipeIngredientRelation?

    @Transaction
    @Query("Select * from recipe_ingredient_table where recipeId = :id")
    fun getRecipeIngredientsByRecipeId(id: Long): Flow<List<FullRecipeIngredientRelation?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeIngredient(recipeIngredient: RecipeIngredientEntity)

    @Delete
    suspend fun deleteRecipeIngredient(recipeIngredient: RecipeIngredientEntity)

    @Transaction
    @Query("Delete from recipe_ingredient_table where recipeId = :id")
    suspend fun deleteRecipeIngredientByRecipeId(id: Long)

    /*
            measure entity
    */
    @Query("Select * from measure_table")
    fun getMeasures(): Flow<List<MeasureEntity>>

    @Query("Select * from measure_table where measureId = :id")
    suspend fun getMeasureById(id: Int): MeasureEntity?

    @Query("Select measureId from measure_table where name = :name")
    suspend fun getMeasureIdByName(name: String): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasure(measure: MeasureEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeasureReturnId(measure: MeasureEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMeasures(conversions: List<MeasureEntity>)

    @Delete
    suspend fun deleteMeasure(measure: MeasureEntity)

    @Query("Delete From measure_table")
    suspend fun deleteAllMeasures()

    /*
            conversion entity
    */
    @Query("Select * from conversion_table")
    fun getConversions(): Flow<List<ConversionEntity>>

    @Query("Select * from conversion_table where conversionId = :id")
    suspend fun getConversionById(id: Int): ConversionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversion(conversion: ConversionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllConversions(conversions: List<ConversionEntity>)

    @Delete
    suspend fun deleteConversions(conversion: ConversionEntity)

    @Query("Delete From conversion_table")
    suspend fun deleteAllConversions()


}