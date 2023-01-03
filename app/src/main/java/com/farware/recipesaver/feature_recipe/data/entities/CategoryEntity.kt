package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Category

@Entity(
    tableName = "category_table",
    foreignKeys = [
        ForeignKey(entity = CategoryColorEntity::class, parentColumns = ["categoryColorId"], childColumns = ["colorId"], onDelete = ForeignKey.SET_DEFAULT)   //
    ],
    indices = [Index("colorId")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long? = null,
    val name: String,
    val colorId: Long,
    val timeStamp: Long,
) {
    companion object {
        fun from(category: Category): CategoryEntity {
            return CategoryEntity(
                category.categoryId,
                category.name,
                category.colorId,
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
            colorId,
            timeStamp
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidCategoryEntityException(message: String): Exception(message)
