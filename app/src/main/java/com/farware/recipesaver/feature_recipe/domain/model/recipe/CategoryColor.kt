package com.farware.recipesaver.feature_recipe.domain.model.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_color")
data class CategoryColor(
    @PrimaryKey(autoGenerate = true)
    val categoryColorId: Long? = null,
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

class InvalidCategoryColorException(message: String): Exception(message)
