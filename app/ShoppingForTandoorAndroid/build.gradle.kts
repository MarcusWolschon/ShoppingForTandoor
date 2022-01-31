plugins {
    id("org.jetbrains.compose") version "1.0.0"
    id("com.android.application")
    kotlin("android")
}

group = "biz.wolschon.tandoorshopping"
version = "v0.1.2"
val projectVersionCode = 12


dependencies {
    implementation(project(":ShoppingForTandoorCommon"))

    val androidxAppcompatVersion: String by project
    implementation("androidx.appcompat:appcompat:$androidxAppcompatVersion")
    val androidxCoreVersion: String by project
    implementation("androidx.core:core-ktx:$androidxCoreVersion")
    val navigationComposeVersion: String by project
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.ui)

    //https://developer.android.com/jetpack/androidx/releases/activity
    implementation("androidx.activity:activity-compose:1.4.0")

    // Bugfix "Can only use lower 16 bits for requestCode"
    implementation("androidx.activity:activity-ktx:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.4.1")
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
    defaultConfig {
        applicationId = "biz.wolschon.tandoorshopping.android"
        //activity = "MainActivity"
        minSdk = getSafeRootProperty("minSdk", 26)
        targetSdk =  getSafeRootProperty("targetSdk", 30)
        versionCode = projectVersionCode
        versionName = project.version.toString()

        buildConfigField( "String", "VERSION_NAME", "\"${ project.version}\"")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    //lint {
    //    isIgnoreTestSources = true
    //}
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}
