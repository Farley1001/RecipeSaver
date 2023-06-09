package com.farware.recipesaver.feature_recipe.domain.use_cases.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)