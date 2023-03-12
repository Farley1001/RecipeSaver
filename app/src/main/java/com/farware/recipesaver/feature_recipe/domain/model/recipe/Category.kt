package com.farware.recipesaver.feature_recipe.domain.model.recipe

data class Category(
    val categoryId: Long? = null,
    val name: String,
    val lightThemeColor: Int,
    val onLightThemeColor: Int,
    val darkThemeColor: Int,
    val onDarkThemeColor: Int,
    val timeStamp: Long,
) {
    /**
     * create any functions that are needed to operate on this entity
     */
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

class InvalidCategoryException(message: String): Exception(message)
