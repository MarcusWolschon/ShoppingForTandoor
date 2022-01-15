pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    pluginManagement {
        val kotlinVersion: String by settings
        plugins {
            id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        }

        plugins {
            id("org.jetbrains.kotlin.jvm") version kotlinVersion
        }

        val pluginComposeVersion: String by settings
        plugins {
            id("org.jetbrains.compose") version pluginComposeVersion
        }
    }
    
}
rootProject.name = "ShoppingForTandoori"


include(":ShoppingForTandoorAndroid")
include(":ShoppingForTandoorDesktop")
include(":ShoppingForTandoorCommon")

