package com.farware.recipesaver.feature_recipe.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// this file contains all the code necessary for the media query option

// to use in a composable
// MediaQuery(Dimensions.Width lessThan 400.dp) {
//      Text(
//          modifier = Modifier
//          .background(Color.Green)
//          text = "I am visible when width is < 400dp by using a MediaQuery"
//      )
//}

// or use in a modifier

//    Text(
//        modifier = Modifier
//        .background(Color.Green)
//        .mediaQuery(
//            Dimensions.Width lessThan 400.dp,
//            modifier = Modifier
//            .background(Color.Blue)
//            .size(300.dp)
//        ),
//        text = "I am visible when width is < 400dp by using a MediaQuery"
//
//    )



sealed class Dimensions {
    object Width: Dimensions()
    object Height: Dimensions()

    sealed class DimensionOperator {
        object LessThan: DimensionOperator()
        object GreaterThan: DimensionOperator()
    }

    class DimensionsComparator(
        val operator: DimensionOperator,
        private val dimension: Dimensions,
        val value: Dp
    ) {
        fun compare(screenWidth: Dp, screenHeight: Dp): Boolean {
            return if(dimension is Width){
                when(operator) {
                    is DimensionOperator.LessThan -> screenWidth < value
                    is DimensionOperator.GreaterThan -> screenWidth > value
                }
            } else {
                when(operator) {
                    is DimensionOperator.LessThan -> screenHeight < value
                    is DimensionOperator.GreaterThan -> screenHeight > value
                }
            }
        }
    }
}

@Composable
fun MediaQuery(comparator: Dimensions.DimensionsComparator, content: @Composable () -> Unit) {
    // get current device screen width and height
    val screenWidth = LocalContext.current.resources.displayMetrics.widthPixels.dp /
            LocalDensity.current.density
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels.dp /
            LocalDensity.current.density

    if(comparator.compare(screenWidth, screenHeight)) {
        content()
    }
}

infix fun Dimensions.lessThan(value: Dp): Dimensions.DimensionsComparator {
    return Dimensions.DimensionsComparator(
        operator = Dimensions.DimensionOperator.LessThan,
        dimension = this,
        value = value
    )
}

infix fun Dimensions.greaterThan(value: Dp): Dimensions.DimensionsComparator {
    return Dimensions.DimensionsComparator(
        operator = Dimensions.DimensionOperator.GreaterThan,
        dimension = this,
        value = value
    )
}