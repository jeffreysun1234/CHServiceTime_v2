// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath(kotlin("gradle-plugin", version = Versions.kotlin_version))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation_version}")
    }
}

plugins {
    id("com.lazan.dependency-export") version "0.5"
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


