package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Recipe
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Tip

@Entity(
    tableName = "tip_table",
    foreignKeys = [
        ForeignKey(entity = RecipeEntity::class, parentColumns = ["recipeId"], childColumns = ["recipeId"])
    ],
    indices = [Index("recipeId")]
)
data class TipEntity(
    @PrimaryKey(autoGenerate = true)
    val tipId: Long? = null,
    val recipeId: Long,
    val tipNumber: Int,
    val imagePath: String? = null,
    val text: String
) {
    companion object {
        fun from(tip: Tip): TipEntity {
            return TipEntity(
                tip.tipId,
                tip.recipeId,
                tip.tipNumber,
                tip.imagePath,
                tip.text
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toTip(): Tip {
        return Tip(
            tipId,
            recipeId,
            tipNumber,
            imagePath,
            text
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = text
}

class InvalidTipEntityException(message: String): Exception(message)