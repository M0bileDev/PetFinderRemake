plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    id(libs.versions.kotlinKaptLiterals.get())
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.petfinderremake.common"
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

    api(project(":logging"))

    api(libs.androidx.core.ktx)
    api(libs.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.navigation.fragment)
    api(libs.androidx.navigation.ui)
    api(libs.glide)

    api(libs.retrofit2.retrofit)
    api(libs.retrofit2.converter.moshi)
    implementation(libs.retrofit2.rx.java.adapter)
    implementation(libs.okHttp3)
    implementation(libs.okHttp3.loggin.interceptor)
    ksp(libs.moshi)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.rx.java)

    api(libs.androidx.room.runtime)
    api(libs.androidx.room.rx.java)
    ksp(libs.androidx.room.compiler)

    api(libs.junit)
    api(libs.truth)
    api(libs.mockk)
    api(libs.androidx.arch.core)
    api(libs.coroutine.test)
    debugApi(libs.fragment.test)

    api(libs.rx.java)
    api(libs.rx.kotlin)
    api(libs.rx.android)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}