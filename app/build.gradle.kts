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

    buildFeatures {
        viewBinding = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))

    // Firebase Analytics, Auth, and Storage
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    implementation("com.google.android.material:material:1.4.0")

    //Responsive Design for the Images/views
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    implementation("com.github.bumptech.glide:glide:4.12.0")

    implementation("com.github.bumptech.glide:compiler:4.12.0")

    //Resposive Design for the text view
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    // Biometric authentication
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Kotlin script runtime
    implementation(kotlin("script-runtime"))
    //Implementing material design dependency
    implementation("com.google.android.material:material:1.9.0")
    // Checking for the latest version
    implementation("com.google.android.material:material:1.8.0")
}