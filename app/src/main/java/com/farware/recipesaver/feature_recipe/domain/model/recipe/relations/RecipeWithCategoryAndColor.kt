package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe

data class RecipeWithCategoryAndColor(
    @Embedded var recipe: Recipe,
    @Relation(
        entity = Category::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryWithColor,
)
