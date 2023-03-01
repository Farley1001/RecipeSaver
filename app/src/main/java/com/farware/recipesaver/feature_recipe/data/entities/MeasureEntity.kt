package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Measure

/*
 * This entity contains the measures selected from a master list on firebase
 * and used in a local recipe.
*/
@Entity(
    tableName = "measure_table",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class MeasureEntity(
    @PrimaryKey(autoGenerate = false)
    val measureId: Long? = null,
    val name: String
){
    companion object {
        fun from(measure: Measure): MeasureEntity {
            return MeasureEntity(
                measure.measureId,
                measure.name
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toMeasure(): Measure {
        return Measure(
            measureId,
            name
        )
    }
    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidMeasureEntityException(message: String): Exception(message)
