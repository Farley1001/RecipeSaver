package com.farware.recipesaver.feature_recipe.presentation.splash

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farware.recipesaver.R
@Composable
fun SplashScreen (
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    Log.d(TAG, "SplashScreen: Start")

    SplashContent()
}


@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SplashContent(){
    Surface(modifier = Modifier
        .fillMaxSize()
        // TODO: Fix Color
        .background(MaterialTheme.colorScheme.background)) {
        val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim3)
        var atEnd by remember { mutableStateOf(false) }
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "Your content description",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .size(64.dp)
                .clickable {
                    atEnd = !atEnd
                }
        )

        DisposableEffect(Unit) {
            atEnd = !atEnd
            onDispose {  }
        }
    }
}
