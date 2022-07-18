plugins {
    id("com.google.gms.google-services")
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "io.lowapple.app.android.petking.data"
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    // Libs
    implementation(libs.timber)
    implementation(libs.androidx.core)
    implementation(libs.bundles.kotlin)

    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    // implementation(platform(libs.firebase.bom))
    // implementation(libs.bundles.firebase)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // implementation(project(":flutter"))
    implementation(project(":domain"))
    implementation(project(":shared"))
}