import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.android.build.gradle.tasks.MergeResources

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0")
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "za.co.varsitycollage.st10050487.eventat"
    compileSdk = 34

    defaultConfig {
        applicationId = "za.co.varsitycollage.st10050487.eventat"
        minSdk = 27
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
    implementation(libs.play.services.maps)
    implementation(libs.places)
    implementation(libs.androidx.biometric.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Responsive Design for the Images/views
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    //Resposive Design for the text view
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    // Add the Biometric library dependency
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    // Add the dependencies for Firebase products you want to use
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")
}