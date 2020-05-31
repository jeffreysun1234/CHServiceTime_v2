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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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

}

dependencies {
    kapt("androidx.room:room-compiler:${Versions.ROOM_VERSION}")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_VERSION}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_VERSION}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION_VERSION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION_VERSION}")
    implementation("androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW_VERSION}")
    implementation("androidx.room:room-runtime:${Versions.ROOM_VERSION}")
    implementation("androidx.room:room-ktx:${Versions.ROOM_VERSION}")
    // implementation("androidx.work:work-gcm:${BuildDependenciesVersions.WORK_VERSION}")
    implementation("androidx.work:work-runtime-ktx:${Versions.WORK_VERSION}")
    implementation("com.google.android.material:material:${Versions.MATERIAL_VERSION}")
    implementation("com.jakewharton.timber:timber:${Versions.TIMBER_VERSION}")
    implementation("org.koin:koin-android:${Versions.KOIN_VERSION}")
    implementation("org.koin:koin-androidx-scope:${Versions.KOIN_VERSION}")
    implementation("org.koin:koin-androidx-viewmodel:${Versions.KOIN_VERSION}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN_VERSION}")
    testImplementation("androidx.room:room-testing:${Versions.ROOM_VERSION}")
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.CORE_TESTING_VERSION}")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    // androidTestImplementation("androidx.work:work-testing:${BuildDependenciesVersions.WORK_VERSION}")

}
