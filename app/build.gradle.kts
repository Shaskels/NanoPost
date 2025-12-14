plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.nanopost"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.nanopost"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+WhenGuards")
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    implementation(project(":util:datetime"))
    implementation(project(":util:networkchecker"))
    implementation(project(":component:uicomponents"))
    implementation(project(":shared:network"))
    implementation(project(":shared:settings"))
    implementation(project(":shared:auth:domain"))
    implementation(project(":shared:auth:remote"))
    implementation(project(":shared:profile:domain"))
    implementation(project(":shared:profile:remote"))
    implementation(project(":shared:post:domain"))
    implementation(project(":shared:post:remote"))
    implementation(project(":util:image"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:editprofile"))
    implementation(project(":feature:image"))
    implementation(project(":feature:subscribers"))
    implementation(project(":feature:post"))
    implementation(project(":feature:newpost"))
    implementation(project(":feature:searchprofiles"))

    //ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.okhttp)
    implementation(libs.ktor.serialization)

    //hilt
    implementation(libs.hilt.android.runtime)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.compose)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    //navigation3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)

    //timber
    implementation(libs.jakewharton.timber)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.http)

    //splashScreen
    implementation(libs.androidx.core.splashscreen)

    //workManager
    implementation(libs.hilt.android.work)
    implementation(libs.androidx.work.runtime)
    ksp(libs.androidx.hilt.compiler)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    //zoomable
    implementation(libs.net.engawapg.zoomable)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}