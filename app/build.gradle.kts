buildscript {
    val kotlinVersion: String by project
    val sqdelightVersion: String by project
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqdelightVersion")
    }
}

group = "biz.wolschon.tandoorshopping"
version = "1.0"

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}