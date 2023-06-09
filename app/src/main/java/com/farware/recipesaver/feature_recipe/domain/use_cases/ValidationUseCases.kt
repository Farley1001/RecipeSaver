package com.farware.recipesaver.feature_recipe.domain.use_cases

import com.farware.recipesaver.feature_recipe.domain.use_cases.validation.ValidateConfirmPasswordUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.validation.ValidateEmailUseCase
import com.farware.recipesaver.feature_recipe.domain.use_cases.validation.ValidatePasswordUseCase

data class ValidationUseCases(
    val validateEmail: ValidateEmailUseCase,
    val validatePassword: ValidatePasswordUseCase,
    val validateConfirmPassword: ValidateConfirmPasswordUseCase
)