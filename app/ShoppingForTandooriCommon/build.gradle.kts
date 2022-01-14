import org.jetbrains.compose.compose

val logbackVersion: String by project
val ktorVersion: String by project
val kotlinVersion: String by project
val sqdelightVersion: String by project
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") // version defined in gradle.properties and read via settings.gradle.kts version
    //application
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

group = "biz.wolschon.tandoorishopping"
version = "1.0"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sqldelight {
        database("AppDatabase") {
            // Package name used for the generated AppDatabase.kt
            packageName = "biz.wolschon.tandoorishopping.common.model.db"
            version = 1

            // An array of folders where the plugin will read your '.sq' and '.sqm'
            // files. The folders are relative to the existing source set so if you
            // specify ["db"], the plugin will look into 'src/main/db'.
            // Defaults to ["sqldelight"] (src/main/sqldelight)
            sourceFolders = listOf("sqldelight")

            // The directory where to store '.db' schema files relative to the root
            // of the project. These files are used to verify that migrations yield
            // a database with the latest schema. Defaults to null so the verification
            // tasks will not be created.
            schemaOutputDirectory = file("src/commonMain/sqldelight/databases")

            // The dialect version you would like to target
            // Defaults to "sqlite:3.18"
            //dialect = "sqlite:3.24"

            // If set to true, migration files will fail during compilation if there are errors in them.
            // Defaults to false
            verifyMigrations = true
        }
        // For native targets, whether sqlite should be automatically linked.
        // Defaults to true.
        linkSqlite = true
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                val kotlinxSerialisationVersion: String by project
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerialisationVersion")

                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")

                // platform independent JSON
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")

                // DB
                api("com.squareup.sqldelight:runtime:$sqdelightVersion")
                api("com.squareup.sqldelight:coroutines-extensions:$sqdelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                val androidxAppcompatVersion: String by project
                implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")

                val androidxCoreVersion: String by project
                implementation("androidx.core:core-ktx:$androidxCoreVersion")

                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-gson:$ktorVersion")

                // DB
                implementation("com.squareup.sqldelight:android-driver:$sqdelightVersion")

                // navigation (implemented myself in other platforms)
                val navigationComposeVersion: String by project
                implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)

                //newest version: https://github.com/Kotlin/kotlinx.coroutines/releases
                val kotlinxCoroutinesDesktopVersion: String by project
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesDesktopVersion")

                implementation("io.ktor:ktor-client-auth-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")

                // DB
                api("com.squareup.sqldelight:sqlite-driver:$sqdelightVersion")
                api("com.squareup.sqldelight:coroutines-extensions-jvm:$sqdelightVersion")
            }
        }
        val desktopTest by getting
    }
}

// region Helper functions

// sadly functions can not be included via apply { from("../common_functions.gradle.kts") }
/**
 * Get property from gradle.properties
 */
fun getRootProperty(prop: String) = rootProject.ext.get(prop) as String

/**
 * Get property from gradle.properties
 */
fun getRootProperty(prop: String) = Integer.parseInt(rootProject.ext.get(prop).toString())

/**
 * Get property from gradle.properties or default
 */
fun getSafeRootProperty(prop: String, fallback: String) =
    if (rootProject.ext.has(prop)) {
        rootProject.ext.get(prop).toString()
    } else {
        fallback
    }

/**
 * Get property from gradle.properties or default
 */
fun getSafeRootProperty(prop: String, fallback: Int) =
    if (rootProject.ext.has(prop)) {
        Integer.parseInt(rootProject.ext.get(prop).toString())
    } else {
        fallback
    }
// endregion

android {
    compileSdk = getSafeRootProperty("compileSdk", 31)
    buildToolsVersion =  getSafeRootProperty("buildToolsVersion", "31.0.0")
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = getSafeRootProperty("minSdk", 26)
        targetSdk =  getSafeRootProperty("targetSdk", 30)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = false // Class Common.BuildConfig is defined multiple times
    }

    //TODO add Manifest file sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

//application {
//    mainClass.set("MainKt")
//}