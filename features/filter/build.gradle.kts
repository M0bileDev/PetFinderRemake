plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    id(libs.versions.kotlinKaptLiterals.get())
    alias(libs.plugins.androidSafeArgs)
}

android {
    namespace = "com.example.petfinderremake.features.filter"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        lint.targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "com.example.petfinderremake.features.filter.di.HiltTestRunner"

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE.md",
                    "META-INF/LICENSE",
                    "META-INF/NOTICE.md",
                    "META-INF/NOTICE",
                    "META-INF/LICENSE-notice.md"
                )
            )
        }
    }
}

dependencies {

    implementation(project(":common"))

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    androidTestImplementation(libs.dagger.hilt.android.testing)
    kaptAndroidTest(libs.dagger.hilt.compiler)

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    androidTestImplementation(libs.espresso.test)
    androidTestImplementation(libs.espresso.test.contrib)

    ksp(libs.moshi)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}