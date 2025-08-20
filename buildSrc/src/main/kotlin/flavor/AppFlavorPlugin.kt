@file:Suppress("unused")

package flavor

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType


class AppFlavorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("notifier", NotifierExtension::class.java)

        project.configureApplication {
            // 1) Define flavors early
            applyFlavorConfig()

            // 2) Apply service plugins early via Android Components (no afterEvaluate)
            if (extension.autoApplyServicePlugins.get().not()) return@configureApplication
            with(project.extensions.getByType<ApplicationAndroidComponentsExtension>()) {

                // Apply Google plugin if any variant has the "google" flavor (or if you choose to always apply)
                beforeVariants(selector().withFlavor(Flavor.DIMENSION to Flavor.GOOGLE.flavorName)) {
                    if (extension.applyGoogle.get() && extension.shouldApplyForThisEnvironment(project))
                        project.applyFlavorPluginWithValidation(
                            configFilePath = extension.googleServicesPath.get(),
                            flavor = Flavor.GOOGLE,
                            extension = extension
                        )
                }

                // Apply Huawei plugin if any variant has the "huawei" flavor
                beforeVariants(selector().withFlavor(Flavor.DIMENSION to Flavor.HUAWEI.flavorName)) {
                    if (extension.applyHuawei.get() && extension.shouldApplyForThisEnvironment(project))
                        project.applyFlavorPluginWithValidation(
                            configFilePath = extension.huaweiServicesPath.get(),
                            flavor = Flavor.HUAWEI,
                            extension = extension
                        )
                }
            }
        }
    }
}

private fun NotifierExtension.shouldApplyForThisEnvironment(project: Project): Boolean {
    if (applyOnlyMatchingFlavor.get().not()) return true

    // IDE sync: apply to keep sources/resources visible
    val requested = project.gradle.startParameter.taskNames.joinToString(" ").lowercase()
    val isIdeSync = requested.isBlank() ||
        System.getProperty("idea.active") == "true" ||
        System.getProperty("android.injected.invoked.from.ide") == "true"

    return isIdeSync || requested.isNotBlank()
}

private fun Project.applyFlavorPluginWithValidation(
    flavor: Flavor,
    configFilePath: String,
    extension: NotifierExtension
) {
    val pluginId = flavor.pluginId
    if (plugins.hasPlugin(pluginId)) return
    val flavorPath = "src/${flavor.flavorName}/${flavor.serviceFile}"
    val hasFile = file(configFilePath).exists() || file(flavorPath).exists()

    if (extension.validateConfigFiles.get() && !hasFile) {
        logger.warn("⚠️ $configFilePath (or $flavorPath) not found. $pluginId plugin not applied.")
        return
    }
    logger.lifecycle("📱 Applying $pluginId plugin")
    pluginManager.apply(pluginId)
}
