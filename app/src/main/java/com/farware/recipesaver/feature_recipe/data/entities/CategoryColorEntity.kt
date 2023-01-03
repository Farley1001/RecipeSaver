package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.CategoryColor

@Entity(tableName = "category_color_table")
data class CategoryColorEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryColorId: Long? = null,
    val name: String,
    val lightThemeColor: Int,
    val onLightThemeColor: Int,
    val darkThemeColor: Int,
    val onDarkThemeColor: Int,
    val timeStamp: Long,
) {
    companion object {
        fun from(categoryColor: CategoryColor): CategoryColorEntity {
            return CategoryColorEntity(
                categoryColor.categoryColorId,
                categoryColor.name,
                categoryColor.lightThemeColor,
                categoryColor.onLightThemeColor,
                categoryColor.darkThemeColor,
                categoryColor.onDarkThemeColor,
                categoryColor.timeStamp
            )
        }
    }

    /**
     * create any functions that are needed to operate on this entity
     */
    fun toCategoryColor(): CategoryColor {
        return CategoryColor(
            categoryColorId,
            name, lightThemeColor,
            onLightThemeColor,
            darkThemeColor,
            onDarkThemeColor,
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

class InvalidCategoryColorEntityException(message: String): Exception(message)