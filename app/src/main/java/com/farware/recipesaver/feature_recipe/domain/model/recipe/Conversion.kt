package com.farware.recipesaver.feature_recipe.domain.model.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversion")
data class Conversion(
    @PrimaryKey(autoGenerate = true)
    val conversionId: Int,
    val name: String,
    val shortName: String,
    val size: Float,
    val sizeText: String,
    val flozUs: Float,
    val flozImp: Float,
    val mlUs: Float,
    val mlImp: Float
){
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

class InvalidConversionException(message: String): Exception(message)
