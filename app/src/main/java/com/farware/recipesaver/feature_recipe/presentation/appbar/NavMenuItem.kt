package com.farware.recipesaver.feature_recipe.presentation.appbar

import androidx.compose.ui.graphics.vector.ImageVector
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureType
import com.farware.recipesaver.feature_recipe.domain.model.data_store.MeasureUnit

data class NavMenuItem(
    val name: String = "",
    val route: String = "",
    val icon: ImageVector? = null,
    val contentDescription: String = "",
    val hasDivider: Boolean = false
)