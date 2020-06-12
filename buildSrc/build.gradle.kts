plugins {
    // `kotlin-dsl`
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.6"
}

repositories {
    jcenter()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}