plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.firebaseCrashlytics) apply false
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}