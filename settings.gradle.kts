enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
//    repositories {
//        google()
//        mavenCentral()
//        maven(url = "https://storage.googleapis.com/download.flutter.io")
//    }
//}
rootProject.name = "PetKing"
include(":app")
include(":domain")
include(":data")
include(":shared")

// Include the host app project.

//apply { from("./gradle/flutter_settings.gradle") }
//include(":android-sdk-oauth:buildSrc")
//include(":android-sdk-oauth:android-sdk-oauth-google")
