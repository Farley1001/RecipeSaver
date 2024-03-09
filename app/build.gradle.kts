import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.firebaseCrashlytics)
}

android {
    namespace = "com.farware.recipesaver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.farware.recipesaver"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                // Where exported schema location is.
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
            // this was add when these properties were moved into local.properties and commented out in build types

            val local = Properties()
            val localProperties: File = project.rootProject.file("local.properties")
            if (localProperties.exists()) {
                localProperties.inputStream().use {
                    local.load(it)
                    buildConfigField("String", "SERVER_CLIENT_ID", "\"${local.getProperty("SERVER_CLIENT_ID")}\"")
                    buildConfigField("String", "FIREBASE_RTD_ROOT", "\"${local.getProperty("FIREBASE_RTD_ROOT")}\"")
                }
            }
            //var properties: Properties = Properties()
            //properties.load(project.rootProject.file("local.properties").newDataInputStream())
            //buildConfigField("String", "SERVER_CLIENT_ID", "\"${localProperties.getProperty("SERVER_CLIENT_ID")}\"")
            //buildConfigField("String", "FIREBASE_RTD_ROOT", "\"${localProperties.getProperty("FIREBASE_RTD_ROOT")}\"")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            // put into local.properties on 5/2021
            //buildConfigField("String", "SERVER_CLIENT_ID", "\"361106271665-fcoutlca3gsiri8qmmo92kpglh237rn7.apps.googleusercontent.com\"")
            //buildConfigField("String", "FIREBASE_RTD_ROOT", "\"FARWARE0923\"")
        }
        getByName("release") {
            // put into local.properties on 5/2021
            //buildConfigField("String", "SERVER_CLIENT_ID", "\"361106271665-fcoutlca3gsiri8qmmo92kpglh237rn7.apps.googleusercontent.com\"")
            //buildConfigField("String", "FIREBASE_RTD_ROOT", "\"FARWARE0923\"")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // TODO: change to release signing file
            // see docs https://github.com/fossasia/open-event-attendee-android/pull/2353/files/7dfa4e2a5fd0d5ce5ce79ef6634447c71cdb112e
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        kotlin {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"    // 1.4.4   changed ok
    }
    packaging {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Firebase
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    // When using the BoM, you don")t specify versions in Firebase library dependencies
    // Declare the dependency for the Authentication library
    implementation(libs.firebase.auth.ktx)
    // Declare the dependency for the Realtime Database library
    implementation(libs.firebase.database.ktx)
    // Declare the dependencies for the Crashlytics and Analytics libraries
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    // google play services dependency
    implementation(libs.play.services.auth)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    androidTestImplementation(libs.androidx.room.testing)

    // datastore
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // splash screen
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.animation.graphics)

    // color picker
    implementation(libs.alwan)

}