plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.recloudstream.gradle")
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly("com.lagradost:cloudstream3:pre-release")
    implementation("com.github.Blatzar:NiceHttp:0.4.4")
    implementation("org.jsoup:jsoup:1.16.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
}

cloudstream {
    description = "My Test Plugin"
    authors = listOf("bash4qi")
    status = 1
    tvTypes = listOf("Movie", "TvSeries")
    language = "en"
    requiresResources = false
}