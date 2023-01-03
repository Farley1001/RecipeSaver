package com.farware.recipesaver.feature_recipe.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.data.entities.CategoryColorEntity
import com.farware.recipesaver.feature_recipe.data.entities.CategoryEntity
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.CategoryWithColor

data class CategoryWithColorRelation(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "colorId",
        entityColumn = "categoryColorId"
    )
    val categoryColor: CategoryColorEntity
){
    companion object {
        fun from(categoryWithColor: CategoryWithColor): CategoryWithColorRelation {
            return CategoryWithColorRelation(
                CategoryEntity(
                    categoryWithColor.categoryId,
                    categoryWithColor.name,
                    categoryWithColor.colorId,
                    categoryWithColor.timeStamp
                ),
                CategoryColorEntity(
                    categoryWithColor.colorId,
                    categoryWithColor.color,
                    categoryWithColor.lightThemeColor,
                    categoryWithColor.onLightThemeColor,
                    categoryWithColor.darkThemeColor,
                    categoryWithColor.onDarkThemeColor,
                    categoryWithColor.timeStamp
                )
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toCategoryWithColor(): CategoryWithColor {
        return CategoryWithColor(
            category.categoryId,
            category.name,
            category.colorId,
            categoryColor.name,
            categoryColor.lightThemeColor,
            categoryColor.onLightThemeColor,
            categoryColor.darkThemeColor,
            categoryColor.onDarkThemeColor,
            category.timeStamp
        )
    }
}