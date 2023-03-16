import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    //
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

android {
    namespace = "com.farware.recipesaver"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.farware.recipesaver"
        minSdk = 23
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        kotlin {
            kotlinOptions {
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
            }
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        //kotlinCompilerExtensionVersion compose_version
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui:1.4.0-alpha04")
    implementation("androidx.compose.material3:material3:1.1.0-alpha04")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.0-alpha04")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0-alpha04")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.0-alpha04")

    // Firebase
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))
    // When using the BoM, you don")t specify versions in Firebase library dependencies
    // Declare the dependency for the Authentication library
    implementation("com.google.firebase:firebase-auth-ktx")
    // Declare the dependency for the Realtime Database library
    implementation("com.google.firebase:firebase-database-ktx")
    // Declare the dependencies for the Crashlytics and Analytics libraries
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    // google play services dependency
    implementation("com.google.android.gms:play-services-auth:20.4.1")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    //implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")    // removed when adding navigation to viewModels
    implementation("androidx.compose.material:material-icons-extended:1.4.0-alpha04")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Room
    implementation("androidx.room:room-runtime:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.5.0")
    androidTestImplementation("androidx.room:room-testing:2.5.0")

    // datastore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.compose.animation:animation-graphics:1.4.0-alpha04")

    // color picker
    implementation("com.raedapps:alwan:1.0.1")

    // memory leak detector
    //debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}