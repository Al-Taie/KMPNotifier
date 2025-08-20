package flavor

enum class Flavor(val pluginId: String, val serviceFile: String? = null) {
    GOOGLE(
        pluginId = "com.google.gms.google-services",
        serviceFile = "google-services.json"
    ),
    HUAWEI(
        pluginId = "com.huawei.agconnect",
        serviceFile = "agconnect-services.json"
    );

    val flavorName = name.lowercase()

    companion object {
        val flavors = values().map { it.flavorName }
        const val DIMENSION = "manufacture"
    }
}
