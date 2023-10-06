package com.farware.recipesaver.feature_recipe.presentation.share_recipe.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.farware.recipesaver.feature_recipe.presentation.share_recipe.ShareRecipeUiState

@Composable
fun ShareRecipeReceiveContent(
    state: ShareRecipeUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Receive Recipe Content Here")
        Text(text = "connecting: ${state.isConnecting} - connected: ${state.isConnected}")
    }

}