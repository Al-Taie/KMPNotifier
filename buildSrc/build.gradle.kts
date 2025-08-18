@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.1"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(libs.gradle.android)
}

group = "io.github.al-taie.notifier"
version = "2.0.1"

gradlePlugin {
    website = "https://github.com/Al-Taie/KMPNotifier"
    vcsUrl = "https://github.com/Al-Taie/KMPNotifier.git"
    plugins {
        register("NotifierAppPlugin") {
            id = "io.github.al-taie.notifier"
            implementationClass = "flavor.AppFlavorPlugin"
            displayName = "Notifier App Flavor Plugin"
            description = "Flavor configuration for Notifier library consumers"
            tags = listOf("android", "kotlin-multiplatform", "notifications", "firebase", "huawei")
        }

        register("NotifierLibPlugin") {
            id = "io.github.al-taie.notifier.library"
            implementationClass = "flavor.LibFlavorPlugin"
            displayName = "Notifier Library Flavor Plugin"
            description = "Flavor configuration for Notifier library development"
            tags = listOf("android", "kotlin-multiplatform", "notifications", "firebase", "huawei")
        }
    }
}
