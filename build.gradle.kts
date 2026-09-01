plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.5.0" apply false


}