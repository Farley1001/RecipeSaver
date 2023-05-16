package com.farware.recipesaver

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApp: Application() {
// use following to make applicationContext available globally
    // THIS IS NOE DONE USING DAGGER HILT @ApplicationContext DEPENDENCY INJECTION

    init {
        instance = this
    }

    companion object {
        private var instance: RecipeApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = RecipeApp.applicationContext()
    }

/*
    override fun onCreate() {
        super.onCreate()
    }*/
}