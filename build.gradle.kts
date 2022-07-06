buildscript {
    val kotlin_version by extra("1.7.0")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.bundles.plugins)
        // classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
//        maven(url = "https://storage.googleapis.com/download.flutter.io")
        google()
        mavenCentral()
        maven(url = "https://storage.googleapis.com/download.flutter.io")
    }
    afterEvaluate {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                if (configurations.findByName("kotlinCompilerPluginClasspath")
                        ?.dependencies
                        ?.any { it.group == "androidx.compose.compiler" } == true) {
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
                    )
                }
            }
        }
    }
}