@file:Suppress("unused")

package flavor

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ProductFlavor
import flavor.Flavor.DIMENSION
import flavor.Flavor.flavors
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties


fun CommonExtension<*, *, *, *, *, *>.configureFlavor(block: ProductFlavor.() -> Unit) =
    productFlavors {
        flavors.forEach { flavorName -> named(flavorName) { block(this) } }
    }

internal fun CommonExtension<*, *, *, *, *, *>.applyFlavorConfig() {
    flavorDimensions.add(DIMENSION)

    productFlavors {
        flavors.forEach { flavorName ->
            create(flavorName) { dimension = DIMENSION }
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.applyFlavorSourceSet() {
    sourceSets {
        flavors.forEach { flavorName ->
            named(flavorName) {
                val name = flavorName.replaceFirstChar { it.uppercase() }
                kotlin.srcDirs("src/android$name/kotlin")
                manifest.srcFile("src/android$name/AndroidManifest.xml")
            }
        }
    }
}

fun Project.configureLibrary(configure: LibraryExtension.() -> Unit) =
    plugins.withId("com.android.library") {
        extensions.configure<LibraryExtension>(configure)
    }

fun Project.configureApplication(configure: ApplicationExtension.() -> Unit) =
    plugins.withId("com.android.application") {
        extensions.configure<ApplicationExtension>(configure)
    }

fun DependencyHandlerScope.flavorApi(
    flavorName: String,
    dependency: Any
) = add(
    configurationName = "${flavorName}Api",
    dependencyNotation = dependency
)

fun getLocalProperty(key: String, file: String? = null, root: String = "."): String {
    val properties = Properties()
    val defaultFiles = listOf("$root/local.properties", "$root/defaults.properties")
    val files = (defaultFiles + file).mapNotNull { it }

    files.forEach {
        val localProperties = File(it)
        if (localProperties.isFile) {
            runCatching {
                InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                    if (properties[key] in listOf(null, "")) properties.load(reader)
                }
            }
        }
    }

    return properties.getProperty(key).toString()
}

fun Project.getKey(key: String) = getLocalProperty(key, root = rootDir.path)
fun Project.getManifestPlaceholdersKey(key: String) = getKey(key).replace("\"", "")
