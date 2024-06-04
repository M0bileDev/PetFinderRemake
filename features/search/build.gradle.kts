plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    id(libs.versions.kotlinKaptLiterals.get())
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.petfinderremake.features.search"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig{
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

dependencies{

    implementation(project(":common"))

    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}