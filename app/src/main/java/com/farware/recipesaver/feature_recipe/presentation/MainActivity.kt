package com.farware.recipesaver.feature_recipe.presentation

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.farware.recipesaver.RecipeApp
import com.farware.recipesaver.feature_recipe.presentation.ui.theme.RecipeSaverTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var app: RecipeApp

    @Inject
    lateinit var randomString: String

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var useDynamicColor = viewModel.state.value.useDynamicColor
            RecipeSaverTheme(dynamicColor = useDynamicColor) {


                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(key1 = lifecycleOwner) {
                    val observer = LifecycleEventObserver{ _, event ->
                        if(event == Lifecycle.Event.ON_PAUSE) {
                            println("on pause called.")
                        }
                        if(event == Lifecycle.Event.ON_RESUME) {
                            println("on resume called.")
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }

                }

                println("onCreate: the app context: $app")
                println("onCreate: $randomString")



                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Navigation()
                }
            }
        }
    }
}