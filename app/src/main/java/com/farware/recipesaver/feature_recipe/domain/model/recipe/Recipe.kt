package com.farware.recipesaver.feature_recipe.domain.model.recipe

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipe",
    foreignKeys = [
        ForeignKey(entity = Category::class, parentColumns = ["categoryId"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_DEFAULT)
    ],
    indices = [
        Index(value = ["categoryId"], unique = false),
        Index(value = ["name"], unique = true)
    ]
)
data class Recipe(
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
    /**
     * create any functions that are needed to operate on this entity
     */
    /*fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }*/

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidRecipeException(message: String): Exception(message)
