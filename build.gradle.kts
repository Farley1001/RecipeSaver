buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

plugins {
    id("com.android.application") version "8.2.0-alpha03" apply false         // 8.1.4 changed ok
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false    // 1.8.10 changed ok
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false     // added
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}