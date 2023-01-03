package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe

@Entity(
    tableName = "recipe_table",
    foreignKeys = [
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["categoryId"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_DEFAULT)
    ],
    indices = [
        Index(value = ["categoryId"], unique = false),
        Index(value = ["name"], unique = true)
    ]
)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long? = null,
    val categoryId: Long,
    val name: String,
    val description: String,
    val imagePath: String? = null,
    val prepTime: Long? = 0,
    val cookTime: Long? = 0,
    val favorite: Boolean? = false,
    val timeStamp: Long,
) {
    companion object {
        fun from(recipe: Recipe): RecipeEntity {
            return RecipeEntity(
                recipe.recipeId,
                recipe.categoryId,
                recipe.name,
                recipe.description,
                recipe.imagePath,
                recipe.prepTime,
                recipe.cookTime,
                recipe.favorite,
                recipe.timeStamp
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
    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidRecipeEntityException(message: String): Exception(message)