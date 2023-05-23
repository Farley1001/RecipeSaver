package com.farware.recipesaver.feature_recipe.domain.model.recipe.relations

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe


data class RecipeWithCategory(
    val recipeId: Long? = null,
    val categoryId: Long,
    val name: String,
    val description: String,
    val imagePath: String? = null,
    val prepTime: Long? = 0,
    val cookTime: Long? = 0,
    val favorite: Boolean? = false,
    val category: String,
    val colorId: Long,
    val color: String,
    val lightThemeColor: Int,
    val onLightThemeColor: Int,
    val darkThemeColor: Int,
    val onDarkThemeColor: Int,
    val timeStamp: Long,
) {

    companion object {
        fun new(): RecipeWithCategory {
            return RecipeWithCategory(
            recipeId = null,
            categoryId = -1L,
            name = "",
            description = "",
            imagePath = null,
            prepTime = 0,
            cookTime = 0,
            favorite = false,
            category = "",
            colorId = -1L,
            color = "",
            lightThemeColor = -1,
            onLightThemeColor = -1,
            darkThemeColor = -1,
            onDarkThemeColor = -1,
            timeStamp = System.currentTimeMillis(),
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipe(): Recipe {
        return Recipe(
            recipeId,
            categoryId,
            name,
            description,
            imagePath,
            prepTime,
            cookTime,
            favorite,
            timeStamp
        )
    }

    fun background(isDarkTheme: Boolean): Int {
        return if(isDarkTheme) darkThemeColor else lightThemeColor
    }

    fun onBackground(isDarkTheme: Boolean): Int {
        return if(isDarkTheme) onDarkThemeColor else onLightThemeColor
    }
    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}
