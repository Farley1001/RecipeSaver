package com.farware.recipesaver.feature_recipe.domain.use_cases.validation

class ValidateConfirmPasswordUseCase {

    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if(password.isNullOrEmpty() ||  password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}