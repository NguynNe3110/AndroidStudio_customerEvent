plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    //SafeArgs (Kotlin) - Navigation
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.uzuu.customer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.uzuu.customer"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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

    // ViewModel + viewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // repeatOnLifecycle + lifecycleScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Activity KTX ,Kotlin extension: by viewModels()
    implementation("androidx.activity:activity-ktx:1.9.3")


// retrofit (thư viện gọi API HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // converter-gson (parse JSON to Domain model)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    //async không block UI
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

// Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp(libs.androidx.room.compiler)

// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

// RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

//Material
    implementation("com.google.android.material:material:1.12.0")

// Navigation (Fragment-based)
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.7")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
}