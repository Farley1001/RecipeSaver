package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category

@Entity(
    tableName = "category_table",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long? = null,
    val name: String,
    val lightThemeColor: Int,
    val onLightThemeColor: Int,
    val darkThemeColor: Int,
    val onDarkThemeColor: Int,
    val timeStamp: Long,
) {
    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                category.categoryId,
                category.name,
                category.lightThemeColor,
                category.onLightThemeColor,
                category.darkThemeColor,
                category.onDarkThemeColor,
                category.timeStamp
            )
        }
    }

    /**
     * create any functions that are needed to operate on this entity
     */
    fun toCategory(): Category {
        return Category(
            categoryId,
            name,
            lightThemeColor,
            onLightThemeColor,
            darkThemeColor,
            onDarkThemeColor,
            timeStamp
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidCategoryEntityException(message: String): Exception(message)
