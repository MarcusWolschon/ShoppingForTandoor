pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    
}
rootProject.name = "ShoppingForTandoori"


include(":ShoppingForTandooriAndroid")
include(":ShoppingForTandooriDesktop")
include(":ShoppingForTandooriCommon")

