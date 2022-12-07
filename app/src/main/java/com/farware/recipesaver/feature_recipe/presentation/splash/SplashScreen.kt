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
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.farware.recipesaver.R
import com.farware.recipesaver.feature_recipe.presentation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (
    navController: NavController,
    splashViewModel: SplashViewModel = viewModel()
) {
    Log.d(TAG, "SplashScreen: Start")
    LaunchedEffect(key1 = true) {
        delay(1000L)                        // changed from 1000L
        if(splashViewModel.currentUser != null) {
            // navigate to recipes screen
            navController.navigate(Screen.RecipesScreen.route)
            splashViewModel.currentUser = null
        } else {
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    SplashContent()
}


@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun SplashContent(){
    Surface(modifier = Modifier
        .fillMaxSize()
        // TODO: Fix Color
        .background(Color.White)) {
        val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim3)
        var atEnd by remember { mutableStateOf(false) }
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = "Your content description",
            modifier = Modifier
                .background(Color.White)
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
