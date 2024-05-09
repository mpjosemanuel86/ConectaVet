plugins {

    id ("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.mpjosemanuel86.conectavet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mpjosemanuel86.conectavet"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    implementation ("com.airbnb.android:lottie:3.4.2")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.material.v140)
    implementation(libs.firebase.auth)
    implementation("com.google.firebase:firebase-database:19.6.0")

    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-analytics")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}