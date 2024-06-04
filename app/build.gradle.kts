plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.androidSafeArgs)
    alias(libs.plugins.googleServices)
    id(libs.versions.kotlinKaptLiterals.get())
}

android {
    namespace = "com.example.petfinderremake"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.petfinderremake"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    implementation(project(":features:discover"))
    implementation(project(":features:search"))
    implementation(project(":features:follow"))
    implementation(project(":features:filter"))
    implementation(project(":features:gallery"))
    implementation(project(":features:notifications"))
    implementation(project(":features:details:animals"))
    implementation(project(":common"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    ksp(libs.androidx.room.compiler)
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
}