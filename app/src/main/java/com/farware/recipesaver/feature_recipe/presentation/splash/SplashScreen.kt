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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.booleanResource
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
        .background(Color.White)
    ){
        val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_anim)
        var atEnd by remember { mutableStateOf(false) }
        // easily check if a tablet
        //
        // set up 2 dimensions in res called is_tablet in
        // the first resource is <bool with name="is_tablet">false
        // on the other select smallest screen width and set to 600
        // this resource is <bool with name="is_tablet">true
        //
        val isTablet = booleanResource(id = R.bool.is_tablet)

        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            //painter = painterResource(id = R.drawable.monica),
            contentDescription = "Your content description",
            modifier = Modifier
                .background(Color.White)
                .padding(20.dp)
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
