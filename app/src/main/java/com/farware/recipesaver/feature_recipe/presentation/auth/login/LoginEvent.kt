package com.farware.recipesaver.feature_recipe.presentation.auth.login

import android.content.Context
import androidx.compose.ui.focus.FocusState

sealed class LoginEvent {
    data class SignInWithEmailAndPassword(val email: String, val password: String): LoginEvent()
    data class SignInWithGoogle(val token: String): LoginEvent()
    data class UpdatePreferences(val context: Context, val displayEmail: String, val displayName: String): LoginEvent()
    data class EmailValueChange(val value: String): LoginEvent()
    data class EmailFocusChange(val focusState: FocusState): LoginEvent()
    data class PasswordValueChange(val value: String): LoginEvent()
    data class PasswordFocusChange(val focusState: FocusState): LoginEvent()
    object ClearEmail: LoginEvent()
    object ClearPassword: LoginEvent()
    object Register: LoginEvent()
}
