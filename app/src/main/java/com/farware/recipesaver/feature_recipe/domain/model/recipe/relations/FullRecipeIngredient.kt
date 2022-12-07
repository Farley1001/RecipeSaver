package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure

data class FullRecipeIngredient(
    @Embedded var recipeIngredientWithIngredient: RecipeIngredientWithIngredient,
    @Relation(
        entity = Measure::class,
        parentColumn = "measureId",
        entityColumn = "measureId"
    )
    val measure: Measure
)
