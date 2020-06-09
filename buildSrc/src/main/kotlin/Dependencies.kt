import org.gradle.api.artifacts.dsl.DependencyHandler

// object TestDependencies {
//     const val ROBOELECTRIC = "org.robolectric:robolectric:${Versions.ROBOELECTRIC}"
//     const val ROOM = "androidx.room:room-testing:${Versions.ROOM}"
//     const val EXT = "androidx.test.ext:junit:${Versions.EXT}"
//     const val CORE = "androidx.test:core:${Versions.TEST}"
//     const val RUNNER = "androidx.test:runner:${Versions.TEST}"
//     const val RULES = "androidx.test:rules:${Versions.TEST}"
//     const val COROUTINES_TEST =
//         "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
//     const val ARCH_CORE = "androidx.arch.core:core-testing:${Versions.ARCH_CORE}"
//     const val FRAGMENT_TEST = "androidx.fragment:fragment-testing:${Versions.FRAGMENT_TEST}"
//     const val MOCK_WEB_SERVER = "com.squareup.okhttp3:mockwebserver:${Versions.MOCK_WEB_SERVER}"
// }

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

// fun DependencyHandler.addAndroidTestsDependencies() {
//     androidTestImplementation(TestAndroidDependencies.PLAY_CORE)
//     androidTestImplementation(TestAndroidDependencies.LEAKCANARY)
//     androidTestImplementation(TestAndroidDependencies.MOCKITO)
//     androidTestImplementation(TestAndroidDependencies.ESPRESSO)
//     androidTestImplementation(TestAndroidDependencies.RUNNER)
//     androidTestImplementation(TestAndroidDependencies.RULES)
//     androidTestImplementation(TestAndroidDependencies.JUNIT)
//     androidTestImplementation(TestAndroidDependencies.FRAGMENT_TEST)
// }