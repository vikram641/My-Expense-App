plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.expense"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expense"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.activity)
    implementation(libs.volley)
//    implementation(libs.places)
//    implementation(libs.androidx.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Chucker for debugging network traffic
    debugImplementation ("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation ("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

    // MPAndroidChart for show graph ui
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.airbnb.android:lottie:6.4.0")

    implementation("com.github.Dimezis:BlurView:version-2.0.4")





}

kapt {
    correctErrorTypes = true
}