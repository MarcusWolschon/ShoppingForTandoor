import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") // version defined in gradle.properties and read via settings.gradle.kts version
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}

group = "biz.wolschon.tandoorshopping"
version = "1.0.0"

buildConfig {
    buildConfigField("String", "APP_VERSION", provider { "\"${project.version}\"" })
}

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
                implementation(project(":ShoppingForTandoorCommon"))
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
            packageName = "ShoppingForTandoor"
            packageVersion = project.version.toString()
            //macOS {
            //    iconFile.set(project.file("jvmMain/resources/favicon.icns"))
            //}
            windows {
                iconFile.set(project.file("jvmMain/resources/favicon.ico"))
            }
            //linux {
            //    iconFile.set(project.file("jvmMain/resources/favicon.png"))
            //}
        }
    }
}

kotlin.sourceSets.all {
    //languageSettings.optIn("kotlin.RequiresOptIn")
}