package com.farware.recipesaver.feature_recipe.domain.use_cases.validation

class ValidatePasswordUseCase {

    fun execute(password: String): ValidationResult {
        if(password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }
        val result = password.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
            val passwordMatcher = Regex(passwordPattern)
            passwordMatcher.find(password) != null
        } ?: false
        if(!result) {
            return ValidationResult(
                successful = false,
                errorMessage = "* Password must contain at least one of each,\nupper case, lower case, number, and special character."
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}