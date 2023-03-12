package com.farware.recipesaver.feature_recipe.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.farware.recipesaver.feature_recipe.data.entities.CategoryEntity
import com.farware.recipesaver.feature_recipe.data.entities.RecipeEntity
import com.farware.recipesaver.feature_recipe.domain.model.recipe.relations.RecipeWithCategory

data class RecipeWithCategoryRelation(
    @Embedded var recipe: RecipeEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val category: CategoryEntity,
) {
    companion object {
        fun from(recipeWithCategory: RecipeWithCategory): RecipeWithCategoryRelation {
            return RecipeWithCategoryRelation(
                RecipeEntity(
                    recipeWithCategory.recipeId,
                    recipeWithCategory.categoryId,
                    recipeWithCategory.name,
                    recipeWithCategory.description,
                    recipeWithCategory.imagePath,
                    recipeWithCategory.prepTime,
                    recipeWithCategory.cookTime,
                    recipeWithCategory.favorite,
                    recipeWithCategory.timeStamp
                ),
                CategoryEntity(
                    recipeWithCategory.categoryId,
                    recipeWithCategory.category,
                    recipeWithCategory.lightThemeColor,
                    recipeWithCategory.onLightThemeColor,
                    recipeWithCategory.darkThemeColor,
                    recipeWithCategory.onDarkThemeColor,
                    recipeWithCategory.timeStamp
                )

            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipeWithCategory(): RecipeWithCategory {
        return RecipeWithCategory(
            recipe.recipeId,
            recipe.categoryId,
            recipe.name,
            recipe.description,
            recipe.imagePath,
            recipe.prepTime,
            recipe.cookTime,
            recipe.favorite,
            category.name,
            category.categoryId!!,
            category.name,
            category.lightThemeColor,
            category.onLightThemeColor,
            category.darkThemeColor,
            category.onDarkThemeColor,
            recipe.timeStamp
        )
    }
}