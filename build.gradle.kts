buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.40.5")
        classpath ("com.google.gms:google-services:4.3.14")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id "com.android.application" version "7.2.2" apply false
//    id "com.android.library" version "7.2.2" apply false
//    id "org.jetbrains.kotlin.android" version "1.6.21" apply false
//}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}