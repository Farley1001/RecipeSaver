package com.farware.recipesaver.feature_recipe.presentation.auth.register

import android.content.Context
import androidx.compose.ui.focus.FocusState

sealed class RegisterEvent {
    data class SignUpWithEmailAndPassword(val email: String, val password: String): RegisterEvent()
    data class UpdatePreferences(val context: Context, val displayEmail: String, val displayName: String): RegisterEvent()
    data class EmailValueChange(val value: String): RegisterEvent()
    data class EmailFocusChange(val focusState: FocusState): RegisterEvent()
    data class PasswordValueChange(val value: String): RegisterEvent()
    data class PasswordFocusChange(val focusState: FocusState): RegisterEvent()
    data class ConfirmPasswordValueChange(val value: String): RegisterEvent()
    data class ConfirmPasswordFocusChange(val focusState: FocusState): RegisterEvent()
    object ClearEmail: RegisterEvent()
    object ClearPassword: RegisterEvent()
    object ClearConfirmPassword: RegisterEvent()
    object Login: RegisterEvent()
}
