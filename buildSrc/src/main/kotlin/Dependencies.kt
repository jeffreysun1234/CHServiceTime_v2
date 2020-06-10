import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addDependencies() {
    kapt("androidx.room:room-compiler:${Versions.room_version}")
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
    implementation("androidx.work:work-runtime-ktx:${Versions.work_version}")
    implementation("com.google.android.material:material:${Versions.material_version}")
    implementation("com.jakewharton.timber:timber:${Versions.timber_version}")
    implementation("org.koin:koin-android:${Versions.koin_version}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin_version}")
    implementation("org.koin:koin-androidx-viewmodel:${Versions.koin_version}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin_version}")
}

fun DependencyHandler.addDebugDependencies() {
    // Once https://issuetracker.google.com/127986458 is fixed this can be testImplementation
    debugImplementation("androidx.fragment:fragment-testing:${Versions.fragment_version}")
    debugImplementation("com.facebook.stetho:stetho:1.5.1")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:1.5.1")
}

/**
 * Adds all the tests dependencies to specific configuration.
 */
fun DependencyHandler.addTestsDependencies() {
    // Some test rules, e.g. InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:${Versions.arch_core_testing_version}")
    // Test helpers for migrations
    testImplementation("androidx.room:room-testing:${Versions.room_version}")
    testImplementation("com.google.truth:truth:${Versions.truth_version}")
    testImplementation("io.mockk:mockk:${Versions.mockk_version}")
    testImplementation("junit:junit:${Versions.junit_version}")
    testImplementation("org.hamcrest:hamcrest:${Versions.hamcrest_version}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx_coroutines_test_version}")
    testImplementation("org.robolectric:robolectric:${Versions.robolectric_version}")
    // testImplementation(RULES)
    // testImplementation(RUNNER)
    // testImplementation(FRAGMENT_TEST)
    // testImplementation(EXT)
    // testImplementation(MOCK_WEB_SERVER)
}

fun DependencyHandler.addAndroidTestsDependencies() {
    // Some test rules, e.g. InstantTaskExecutorRule
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.arch_core_testing_version}")
    androidTestImplementation("androidx.navigation:navigation-testing:${Versions.navigation_version}")
    // Core library
    androidTestImplementation("androidx.test:core:${Versions.testCore_version}")
    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:${Versions.runner_version}")
    androidTestImplementation("androidx.test:rules:${Versions.rules_version}")
    // Assertions
    androidTestImplementation("androidx.test.ext:junit:${Versions.testExtJUnit_version}")
    androidTestImplementation("androidx.test.ext:truth:${Versions.testExtTruth_version}")
    androidTestImplementation("com.google.truth:truth:${Versions.truth_version}")
    // Espresso dependencies
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espresso_version}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.espresso_version}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Versions.espresso_version}")
    androidTestImplementation("androidx.test.espresso:espresso-accessibility:${Versions.espresso_version}")
    androidTestImplementation("androidx.test.espresso.idling:idling-concurrent:${Versions.espresso_version}")
    // The following Espresso dependency can be either "implementation"
    // or "androidTestImplementation(", depending on whether you want the
    // dependency to appear on your APK's compile classpath or the test APK
    // classpath.
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.1.0")
    androidTestImplementation("androidx.work:work-testing:${Versions.work_version}")
    androidTestImplementation("io.mockk:mockk:${Versions.mockk_version}")
    androidTestImplementation("junit:junit:${Versions.junit_version}")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx_coroutines_test_version}")
    androidTestImplementation("org.koin:koin-test:${Versions.koin_version}")

    // androidTestImplementation(TestAndroidDependencies.PLAY_CORE)
    // androidTestImplementation(TestAndroidDependencies.LEAKCANARY)
}