plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.savr"
    compileSdk = 35

    defaultConfig {
        // ... your existing stuff
        buildConfigField(
            "String",
            "PEXELS_API_KEY",
            "\"${project.findProperty("PEXELS_API_KEY")}\""
        )
    }


    defaultConfig {
        applicationId = "com.example.savr"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"${project.findProperty("GEMINI_API_KEY")}\""
        )
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
        compose = true
        buildConfig = true
    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.generativeai)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.recyclerview)
        implementation(libs.generativeai.v021)
        implementation(libs.androidx.recyclerview)
        implementation(libs.androidx.appcompat)
        implementation(libs.picasso)
        implementation(libs.gson)
        implementation(libs.androidx.cardview)
        implementation(libs.generativeai.v060)
        implementation(libs.generativeai)
        implementation(libs.material)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
        implementation("androidx.recyclerview:recyclerview:1.2.0")
        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    }
}