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
    id("com.vanniktech.android.junit.jacoco") version "0.16.0"
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

configure<com.vanniktech.android.junit.jacoco.JunitJacocoExtension> {
    jacocoVersion = "0.8.5"
    excludes // type String List
    includeNoLocationClasses = false // type boolean
    includeInstrumentationCoverageInMergedReport = false // type boolean
}


