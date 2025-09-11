// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // This setting forces all repository declarations into this file.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add JitPack here for library dependencies.
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "B.Tech Buddy"
include(":app")
