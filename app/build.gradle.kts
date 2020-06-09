import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "com.mycompany.chservicetime"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    sourceSets {
        val sharedTestDir = "src/sharedTest/java"
        getByName("test").java.srcDir(sharedTestDir)
        getByName("androidTest").java.srcDir(sharedTestDir)
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
            isUseProguard = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            testProguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            isUseProguard = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            testProguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // work-runtime-ktx 2.1.0 and above now requires Java 8
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
            all(KotlinClosure1<Any, Test>({
                (this as Test).also {
                    testLogging {
                        events = mutableSetOf(
                            TestLogEvent.FAILED,
                            TestLogEvent.PASSED,
                            TestLogEvent.SKIPPED,
                            TestLogEvent.STANDARD_OUT,
                            TestLogEvent.STANDARD_ERROR
                        )
                    }
                }
            }, unitTests))
        }
    }
    dataBinding {
        isEnabledForTests = true
    }
}

dependencies {
    debugImplementation("com.facebook.stetho:stetho:1.5.1")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:1.5.1")
    kapt("androidx.room:room-compiler:${Versions.room_version}")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.annotation:annotation:${Versions.annotation_version}")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigation_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigation_version}")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview_version}")
    implementation("androidx.room:room-runtime:${Versions.room_version}")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${Versions.room_version}")
// implementation("androidx.work:work-gcm:${BuildDependenciesVersions.WORK_VERSION}")
    implementation("androidx.work:work-runtime-ktx:${Versions.work_version}")
    implementation("com.google.android.material:material:${Versions.material_version}")
    implementation("com.jakewharton.timber:timber:${Versions.timber_version}")
    implementation("org.koin:koin-android:${Versions.koin_version}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin_version}")
    implementation("org.koin:koin-androidx-viewmodel:${Versions.koin_version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin_version}")
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.arch_core_testing_version}")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
// androidTestImplementation("androidx.work:work-testing:${BuildDependenciesVersions.WORK_VERSION}")

    addTestsDependencies()

}
