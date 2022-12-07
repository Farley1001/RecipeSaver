package com.farware.recipesaver

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}