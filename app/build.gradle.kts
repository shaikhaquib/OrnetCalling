plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dis.ornetcalling"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dis.ornetcalling"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Coroutine Lifecycle Scopes
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Activity KTX for viewModels()
    implementation(libs.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.kotlinx.metadata.jvm)

    implementation(libs.androidx.lifecycle.extensions)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)


    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)

    //Custom Toast
    implementation(libs.toasty)

    // Chucker
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    // Google Maps
    implementation(libs.play.services.maps)
    implementation(libs.maps.utils.ktx)
    implementation(libs.android.maps.utils)
    implementation(libs.play.services.location)

    // Google Translator
    implementation(libs.translate)

    // Google SMS retrieval API
    implementation(libs.play.services.auth)
    implementation(libs.play.services.auth.api.phone)

    // Timber
    implementation(libs.timber)

    // Lottie Animation
    implementation(libs.lottie)

    // Firebase
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.core)
    implementation(libs.firebase.messaging.ktx)

    // Image Loader
    implementation(libs.coil)

    // RxJava
    implementation(libs.rxandroid)

    // EventBus
    implementation(libs.eventbus)

    //Crash Detection
    implementation(libs.customactivityoncrash)

    implementation(libs.image.picker)
}