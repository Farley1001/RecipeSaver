package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category
import com.farware.recipesaver.feature_recipe.domain.model.recipe.CategoryColor

data class CategoryWithColor(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "colorId",
        entityColumn = "categoryColorId"
    )
    val categoryColor: CategoryColor
)
