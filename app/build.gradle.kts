import java.util.Properties // Import required for Properties class

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.map.secret)
}

android {
    namespace = "za.co.varsitycollage.st10050487.eventat"
    compileSdk = 34

    buildFeatures {
        buildConfig = true  // Enable BuildConfig feature
    }

    defaultConfig {
        applicationId = "za.co.varsitycollage.st10050487.eventat"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Loading the MAPS_KEY from local.properties
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
            val mapsApiKey = properties["MAPS_KEY"] as String
            buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.storage)
    implementation(libs.play.services.tflite.support)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    // Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    // Dependency for Bio-metric
    implementation("androidx.biometric:biometric:1.2.0-alpha01")
    implementation("androidx.asynclayoutinflater:asynclayoutinflater-appcompat:1.1.0-alpha01")
    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.compose.material:material:1.0.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.5")
    implementation("androidx.activity:activity-compose:1.3.1")

    implementation(libs.play.services.maps.v1700)
    implementation("com.google.android.gms:play-services-maps:18.0.0")
    implementation("com.google.android.libraries.places:places:2.4.0")
    //Implementing the Volley Dependency
    implementation("com.android.volley:volley:1.2.1")

}
