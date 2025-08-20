package flavor

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import javax.inject.Inject


abstract class NotifierExtension @Inject constructor(
    objects: ObjectFactory,
    providers: ProviderFactory
) {
    // Apply both service plugins automatically
    val autoApplyServicePlugins: Property<Boolean> = objects.createBoolean(
        providers = providers,
        propertyName = "notifier.autoApply",
        defaultValue = true
    )

    // Per-service toggles
    val applyGoogle: Property<Boolean> = objects.createBoolean(
        providers = providers,
        propertyName = "notifier.applyGoogle",
        defaultValue = true
    )

    val applyHuawei: Property<Boolean> = objects.createBoolean(
        providers = providers,
        propertyName = "notifier.applyHuawei",
        defaultValue = true
    )

    // Only apply the service plugin(s) that match the requested flavor in the task names
    // In IDE sync we apply both so sources/resources are visible.
    val applyOnlyMatchingFlavor: Property<Boolean> = objects.createBoolean(
        providers = providers,
        propertyName = "notifier.applyOnlyMatchingFlavor",
        defaultValue = true
    )

    // Optional: validate that config files exist before applying the plugin(s)
    val validateConfigFiles: Property<Boolean> = objects.createBoolean(
        providers = providers,
        propertyName = "notifier.validateConfigFiles",
        defaultValue = true
    )

    // Optional: custom paths (relative to projectDir)
    val googleServicesPath: Property<String> = objects.createString(
        providers = providers,
        propertyName = "notifier.googleServicesPath",
        defaultValue = "google-services.json"
    )

    val huaweiServicesPath: Property<String> = objects.createString(
        providers = providers,
        propertyName = "notifier.huaweiServicesPath",
        defaultValue = "agconnect-services.json"
    )
}
