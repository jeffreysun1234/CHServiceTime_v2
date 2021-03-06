import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
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
    signingConfigs {
        create("release") {
            // add these parameters related the signature to local.properties in the ROOT path.
            storeFile = file(gradleLocalProperties(rootDir).getProperty("storeFile"))
            storePassword = gradleLocalProperties(rootDir).getProperty("storePassword")
            keyAlias = gradleLocalProperties(rootDir).getProperty("keyAlias")
            keyPassword = gradleLocalProperties(rootDir).getProperty("keyPassword")
        }
    }
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = "30.0.0"

    defaultConfig {
        applicationId = "com.mycompany.servicetime"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = 10
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        // // The following argument makes the Android Test Orchestrator run its
        // // "pm clear" command after each test invocation. This command ensures
        // // that the app's state is completely cleared between tests.
        // testInstrumentationRunnerArguments = mapOf(
        //     "clearPackageData" to "true"
        // )

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    // val sharedTestDir = "src/sharedTest/java"
    // sourceSets["test"].java.srcDir(sharedTestDir)
    // sourceSets["androidTest"].java.srcDir(sharedTestDir)
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
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            testProguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            testProguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        // execution = "ANDROID_TEST_ORCHESTRATOR"
        animationsDisabled = true
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
    // TODO: which packages make thees duplicate files
    packagingOptions {
        pickFirsts =
            setOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
                "META-INF/licenses/ASM"
            )
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    addDependencies()
    addDebugDependencies()
    addTestsDependencies()
    addAndroidTestsDependencies()
}


