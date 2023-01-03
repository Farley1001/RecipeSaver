package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Ingredient
import com.farware.recipesaver.feature_recipe.domain.model.recipe.RecipeIngredient
import java.math.RoundingMode

@Entity(
    tableName = "recipe_ingredient_table",
    foreignKeys = [
        ForeignKey(entity = RecipeEntity::class, parentColumns = ["recipeId"], childColumns = ["recipeId"]),
        ForeignKey(entity = IngredientEntity::class, parentColumns = ["ingredientId"], childColumns = ["ingredientId"], onDelete = ForeignKey.SET_DEFAULT),
        ForeignKey(entity = MeasureEntity::class, parentColumns = ["measureId"], childColumns = ["measureId"], onDelete = ForeignKey.SET_DEFAULT)
    ],
    indices = [
        Index(value = ["recipeId"], unique = false),
        Index(value = ["ingredientId"], unique = false),
        Index(value = ["measureId"], unique = false)
    ]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeIngredientId: Long? = null,
    val recipeId: Long,
    val ingredientId: Int,
    val measureId: Int,
    val amount: String,
    val measure: String,
    val ingredient: String
) {
    companion object {
        fun from(recipeIngredient: RecipeIngredient): RecipeIngredientEntity {
            return RecipeIngredientEntity(
                recipeIngredient.recipeIngredientId,
                recipeIngredient.recipeId,
                recipeIngredient.ingredientId,
                recipeIngredient.measureId,
                recipeIngredient.amount,
                recipeIngredient.measure,
                recipeIngredient.ingredient
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toRecipeIngredient(): RecipeIngredient {
        return RecipeIngredient(
            recipeIngredientId,
            recipeId,
            ingredientId,
            measureId,
            amount,
            measure,
            ingredient
        )
    }

    private fun amountToString(amount: Float): String {
        var result: String
        val s = amount.toString()
        val i = s.split(".")


        if(i[1].startsWith("0")) {
            // decimal is to small return 0
            result = i[0]
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.125f) {
            // has a decimal of <= .125
            result = i[0] + " 1/8"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.25f) {
            // has a decimal of <= .25
            result = i[0] + " 1/4"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.333f) {
            // has a decimal of <= .333
            result = i[0] + " 1/3"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.5f) {
            // has a decimal of <= .5
            result = i[0] + " 1/2"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.666f) {
            // has a decimal of <= .666
            result = i[0] + " 2/3"
        } else if(("0." + i[1]).toBigDecimal().setScale(3, RoundingMode.HALF_DOWN).toFloat() <= 0.75f) {
            // has a decimal of <= .75
            result = i[0] + " 3/4"
        } else {
            result = i[0]
        }
        return result
    }
    /*
     * override toString() to return amount as the default
     */
    override fun toString() = "$amount $measure $ingredient"
}

class InvalidRecipeIngredientEntityException(message: String): Exception(message)