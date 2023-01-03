package com.farware.recipesaver.feature_recipe.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.data.entities.IngredientEntity
import com.farware.recipesaver.feature_recipe.data.entities.MeasureEntity
import com.farware.recipesaver.feature_recipe.data.entities.RecipeIngredientEntity
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.FullRecipeIngredient

data class FullRecipeIngredientRelation(
    @Embedded var fullRecipeIngredient: RecipeIngredientWithIngredientRelation,
    @Relation(
        entity = MeasureEntity::class,
        parentColumn = "measureId",
        entityColumn = "measureId"
    )
    val measure: MeasureEntity
){
    companion object {
        fun from(fullRecipeIngredient: FullRecipeIngredient): FullRecipeIngredientRelation {
            return FullRecipeIngredientRelation(
                RecipeIngredientWithIngredientRelation(
                    RecipeIngredientEntity(
                        fullRecipeIngredient.recipeIngredientId,
                        fullRecipeIngredient.recipeId,
                        fullRecipeIngredient.ingredientId,
                        fullRecipeIngredient.measureId,
                        fullRecipeIngredient.amount,
                        fullRecipeIngredient.measure,
                        fullRecipeIngredient.ingredient
                    ),
                    IngredientEntity(
                        fullRecipeIngredient.ingredientId,
                        fullRecipeIngredient.ingredientName,
                        fullRecipeIngredient.type
                    )
                ),
                MeasureEntity(
                    fullRecipeIngredient.measureId,
                    fullRecipeIngredient.measure,
                    fullRecipeIngredient.type
                )
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toFullRecipeIngredient(): FullRecipeIngredient {
        return FullRecipeIngredient(
            fullRecipeIngredient.recipeIngredient.recipeIngredientId,
            fullRecipeIngredient.recipeIngredient.recipeId,
            fullRecipeIngredient.recipeIngredient.ingredientId,
            fullRecipeIngredient.recipeIngredient.measureId,
            fullRecipeIngredient.recipeIngredient.amount,
            fullRecipeIngredient.recipeIngredient.measure,
            fullRecipeIngredient.recipeIngredient.ingredient,
            fullRecipeIngredient.ingredient.name,
            fullRecipeIngredient.ingredient.type
        )
    }
}