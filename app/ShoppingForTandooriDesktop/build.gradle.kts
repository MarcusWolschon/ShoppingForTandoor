import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") // version defined in gradle.properties and read via settings.gradle.kts version
}

group = "biz.wolschon.tandoorishopping"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":ShoppingForTandooriCommon"))
                implementation(compose.desktop.currentOs)
                api(compose.preview)

                // https://mvnrepository.com/artifact/commons-logging/commons-logging
                implementation("commons-logging:commons-logging:1.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}

kotlin.sourceSets.all {
    //languageSettings.optIn("kotlin.RequiresOptIn")
}