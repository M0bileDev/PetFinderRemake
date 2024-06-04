plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    id(libs.versions.kotlinKaptLiterals.get())
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.petfinderremake.navigation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        lint.targetSdk = libs.versions.targetSdk.get().toInt()
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
}

dependencies {

    implementation(project(":common"))

    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    api(libs.androidx.navigation.fragment)
    api(libs.androidx.navigation.ui)
    kapt(libs.dagger.hilt.compiler)

}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}