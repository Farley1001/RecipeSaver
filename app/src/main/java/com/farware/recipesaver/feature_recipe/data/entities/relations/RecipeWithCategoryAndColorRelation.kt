package com.farware.recipesaver.feature_recipe.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.data.entities.CategoryColorEntity
import com.farware.recipesaver.feature_recipe.data.entities.CategoryEntity
import com.farware.recipesaver.feature_recipe.data.entities.RecipeEntity
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategoryAndColor

data class RecipeWithCategoryAndColorRelation(
    @Embedded var recipe: RecipeEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryWithColorRelation,
) {
    companion object {
        fun from(recipeWithCategoryAndColor: RecipeWithCategoryAndColor): RecipeWithCategoryAndColorRelation {
            return RecipeWithCategoryAndColorRelation(
                RecipeEntity(
                    recipeWithCategoryAndColor.recipeId,
                    recipeWithCategoryAndColor.categoryId,
                    recipeWithCategoryAndColor.name,
                    recipeWithCategoryAndColor.description,
                    recipeWithCategoryAndColor.imagePath,
                    recipeWithCategoryAndColor.prepTime,
                    recipeWithCategoryAndColor.cookTime,
                    recipeWithCategoryAndColor.favorite,
                    recipeWithCategoryAndColor.timeStamp
                ),
                CategoryWithColorRelation(
                    CategoryEntity(
                        recipeWithCategoryAndColor.categoryId,
                        recipeWithCategoryAndColor.category,
                        recipeWithCategoryAndColor.colorId,
                        recipeWithCategoryAndColor.timeStamp
                    ),
                    CategoryColorEntity(
                        recipeWithCategoryAndColor.colorId,
                        recipeWithCategoryAndColor.color,
                        recipeWithCategoryAndColor.lightThemeColor,
                        recipeWithCategoryAndColor.onLightThemeColor,
                        recipeWithCategoryAndColor.darkThemeColor,
                        recipeWithCategoryAndColor.onDarkThemeColor,
                        recipeWithCategoryAndColor.timeStamp
                    )
                )
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipeWithCategoryAndColor(): RecipeWithCategoryAndColor {
        return RecipeWithCategoryAndColor(
            recipe.recipeId,
            recipe.categoryId,
            recipe.name,
            recipe.description,
            recipe.imagePath,
            recipe.prepTime,
            recipe.cookTime,
            recipe.favorite,
            category.category.name,
            category.category.colorId,
            category.categoryColor.name,
            category.categoryColor.lightThemeColor,
            category.categoryColor.onLightThemeColor,
            category.categoryColor.darkThemeColor,
            category.categoryColor.onDarkThemeColor,
            recipe.timeStamp
        )
    }
}