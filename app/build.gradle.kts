plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.unilibrary"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.unilibrary"
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
}

dependencies {
    // Dependências padrão do Android
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ROOM (Formato correto para usar com Java e o Version Catalog)
    implementation(libs.room.runtime)
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("org.mindrot:jbcrypt:0.4")
}