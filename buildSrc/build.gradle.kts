@file:Suppress("UnstableApiUsage")

plugins {
    `kotlin-dsl`
    alias(libs.plugins.gradle.publish)
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(libs.gradle.android.api)
}

val notifier = libs.plugins.notifier.asProvider().get()
group = notifier.pluginId
version = notifier.version.toString()

gradlePlugin {
    website = "https://github.com/Al-Taie/KMPNotifier"
    vcsUrl = "https://github.com/Al-Taie/KMPNotifier.git"
    plugins {
        register("NotifierAppPlugin") {
            id = notifier.pluginId
            implementationClass = "flavor.AppFlavorPlugin"
            displayName = "Notifier App Flavor Plugin"
            description = "Flavor configuration for Notifier library consumers"
            tags = listOf("android", "kotlin-multiplatform", "notifications", "firebase", "huawei")
        }

        register("NotifierLibPlugin") {
            id = "${notifier.pluginId}.library"
            implementationClass = "flavor.LibFlavorPlugin"
            displayName = "Notifier Library Flavor Plugin"
            description = "Flavor configuration for Notifier library development"
            tags = listOf("android", "kotlin-multiplatform", "notifications", "firebase", "huawei")
        }
    }
}
