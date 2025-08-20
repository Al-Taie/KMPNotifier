import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import flavor.getKey
import flavor.flavorApi
import flavor.Flavor
import maven.configure

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinNativeCocoaPods)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.notifier.library)
    alias(libs.plugins.google.services)
    alias(libs.plugins.dokka)
    id("signing")
}

signing {
    useGpgCmd()
}

kotlin {
    explicitApi()

    androidTarget {
        publishLibraryVariants("googleRelease", "huaweiRelease")
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
            }
        }
    }

    cocoapods {
        ios.deploymentTarget = "14.1"
        framework {
            baseName = Config.LIBRARY_NAME
            isStatic = true
        }
        noPodspec()
        pod("FirebaseMessaging")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.bundles.androidMain)
        }

        commonMain.dependencies {
            implementation(libs.bundles.commonMain)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = Config.PACKAGE_ID
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        flavorApi(flavorName = Flavor.HUAWEI.flavorName, dependency = libs.huawei.push)
        flavorApi(flavorName = Flavor.GOOGLE.flavorName, dependency = libs.firebase.messaging)
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true
        )
    )
    coordinates(
        groupId = Config.ARTIFACT_GROUP_ID,
        artifactId = Config.ARTIFACT_ID,
        version = libs.versions.notifier.get()
    )
    pom {
        configure(
            libraryName = Config.LIBRARY_NAME,
            libraryDescription = Config.LIBRARY_DESCRIPTION,
            developerName = Config.DEVELOPER_NAME,
            developerEmail = getKey("DEVELOPER_EMAIL"),
            githubUsername = Config.GITHUB_USERNAME,
            githubRepositoryName = Config.GITHUB_REPOSITORY_NAME,
        )
    }

    publishToMavenCentral()
    signAllPublications()
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("dokka"))

    dokkaSourceSets {
        configureEach {
            includeNonPublic.set(false)
            skipEmptyPackages.set(true)
            reportUndocumented.set(false) // Changed to false to avoid issues
            skipDeprecated.set(false)
        }
    }
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}
