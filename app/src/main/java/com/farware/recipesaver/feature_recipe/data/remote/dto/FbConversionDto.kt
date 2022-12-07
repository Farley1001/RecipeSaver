package com.farware.recipesaver.feature_recipe.data.remote.dto

import com.farware.recipesaver.feature_recipe.domain.model.recipe.Conversion
import com.squareup.moshi.Json

data class FbConversionDto(
    @field:Json(name = "Flozimp") val Flozimp: Float = 0F,
    @field:Json(name = "Flozus") val Flozus: Float = 0F,
    @field:Json(name = "Id") val Id: Int = -1,
    @field:Json(name = "Mlimp") val Mlimp: Float = 0F,
    @field:Json(name = "Mlus") val Mlus: Float = 0F,
    @field:Json(name = "Name") val Name: String = "",
    @field:Json(name = "ShortName") val ShortName: String = "",
    @field:Json(name = "Size") val Size: Float = 0F,
    @field:Json(name = "SizeText") val SizeText: String = "",
)

fun FbConversionDto.toConversion(): Conversion {
    return Conversion(
        conversionId = Id,
        name = Name,
        shortName = ShortName,
        size = Size,
        sizeText = SizeText,
        flozUs = Flozus,
        flozImp = Flozimp,
        mlUs = Mlus,
        mlImp = Mlimp,
    )
}
