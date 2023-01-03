package com.farware.recipesaver.feature_recipe.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion

@Entity(tableName = "conversion_table")
data class ConversionEntity(
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
    companion object {
        fun from(conversion: Conversion): ConversionEntity {
            return ConversionEntity(
                conversion.conversionId,
                conversion.name,
                conversion.shortName,
                conversion.size,
                conversion.sizeText,
                conversion.flozUs,
                conversion.flozImp,
                conversion.mlUs,
                conversion.mlImp
            )
        }
    }
    /**
     * create any functions that are needed to operate on this entity
     */
    fun toConversion(): Conversion {
        return Conversion(
            conversionId,
            name,
            shortName,
            size,
            sizeText,
            flozUs,
            flozImp,
            mlUs,
            mlImp
        )
    }

    /**
     * override toString() to return name as the default
     */
    override fun toString() = name
}

class InvalidConversionEntityException(message: String): Exception(message)