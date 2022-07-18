plugins {
    id("com.google.gms.google-services")
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    defaultConfig {
        applicationId = "io.lowapple.app.android.petking"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
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
    buildFeatures {
        compose = true
        viewBinding = true
        dataBinding = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.1.0-beta01"
//    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf(
        "include" to listOf("*.jar"), "dir" to "libs"
    )))
    // implementation(fileTree(include:["*.aar"], dir: "libs"))
    // Platform
    implementation(platform(libs.firebase.bom))
    // Bundles
    implementation(libs.androidx.core)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.google)
    implementation(libs.google.service.playservice)
    // Kakao
    implementation(libs.bundles.kakao)

    // Libs
    implementation(libs.timber)

    testImplementation("junit:junit:4.13.2")

    debugImplementation("androidx.compose.ui:ui-tooling:1.1.0-beta01")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.0-beta01")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-beta01")

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // implementation(project(":flutter"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":shared"))
}