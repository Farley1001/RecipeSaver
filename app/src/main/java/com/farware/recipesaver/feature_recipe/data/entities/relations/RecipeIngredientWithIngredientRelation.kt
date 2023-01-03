package com.farware.recipesaver.feature_recipe.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.data.entities.IngredientEntity
import com.farware.recipesaver.feature_recipe.data.entities.RecipeIngredientEntity
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeIngredientWithIngredient

data class RecipeIngredientWithIngredientRelation(
    @Embedded val recipeIngredient: RecipeIngredientEntity,
    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "ingredientId"
    )
    val ingredient: IngredientEntity
) {
    companion object {
        fun from(recipeIngredientWithIngredient: RecipeIngredientWithIngredient): RecipeIngredientWithIngredientRelation {
            return RecipeIngredientWithIngredientRelation(
                RecipeIngredientEntity(
                    recipeIngredientWithIngredient.recipeIngredientId,
                    recipeIngredientWithIngredient.recipeId,
                    recipeIngredientWithIngredient.ingredientId,
                    recipeIngredientWithIngredient.measureId,
                    recipeIngredientWithIngredient.amount,
                    recipeIngredientWithIngredient.measure,
                    recipeIngredientWithIngredient.ingredient
                ),
                IngredientEntity(
                    recipeIngredientWithIngredient.ingredientId,
                    recipeIngredientWithIngredient.ingredientName,
                    recipeIngredientWithIngredient.type
                )
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipeIngredientWithIngredient(): RecipeIngredientWithIngredient {
        return RecipeIngredientWithIngredient(
            recipeIngredient.recipeIngredientId,
            recipeIngredient.recipeId,
            recipeIngredient.ingredientId,
            recipeIngredient.measureId,
            recipeIngredient.amount,
            recipeIngredient.measure,
            recipeIngredient.ingredient,
            ingredient.name,
            ingredient.type
        )
    }
}