@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
//    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    kotlin("plugin.serialization") version "2.2.0"
    id("com.google.gms.google-services")

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            freeCompilerArgs += listOf("-Xbinary=bundleId=com.lovorise")
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)

            implementation (libs.androidx.media3.exoplayer)
           // implementation (libs.androidx.media3.session)
            implementation (libs.androidx.media3.ui)
            implementation (libs.androidx.media3.datastore)
            implementation (libs.androidx.media3.database)
            implementation (libs.androidx.media3.transformer)
            implementation (libs.androidx.media3.common)

            //camerax
            implementation(libs.androidx.camera.core)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.video)
            implementation(libs.androidx.camera.view)

//            implementation("androidx.emoji2:emoji2:1.5.0")
//            implementation("androidx.emoji2:emoji2-views:1.5.0")
            implementation(libs.androidx.emoji2.emojipicker)

            
            //google auth
            implementation(libs.androidx.credentials)
            implementation(libs.googleid)
            implementation(libs.androidx.credentials.play.services.auth) // required for android 13 and below

            //work manager
            implementation(libs.androidx.work.runtime.ktx)

            //gps
            implementation(libs.play.services.location)
            implementation("androidx.browser:browser:1.8.0")
            implementation("com.google.android.play:review-ktx:2.0.2")

            implementation("com.android.billingclient:billing-ktx:7.1.1")
            implementation("com.github.lincollincol:amplituda:2.3.0")

            implementation(libs.koin.android)

            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:33.14.0"))
            implementation("com.google.firebase:firebase-messaging-ktx:24.1.1")


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
//            implementation(libs.navigation.z)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)


            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.bottomSheetNavigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.kotlinx.io.core)


            // implementation("org.jetbrains.compose.material3:material3:1.6.11")

           // implementation("io.github.ismai117:kottie:2.0.0")

            implementation(libs.compottie)
            implementation(libs.compottie.resources)
            implementation(libs.compottie.dot)


            implementation(libs.kotlinx.datetime)

            implementation(libs.koin.compose)
            implementation(libs.koin.composeVM)

            implementation(libs.reorderable)


            implementation(libs.ktor.core)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.video)
            implementation(libs.coil.mp)
            implementation(libs.coil.network.ktor)
            implementation(libs.coil.compose)

            //network state
            implementation(libs.konnectivity)

            //pagination
            implementation(libs.lazyPaginationCompose)
//            implementation(libs.androidx.paging.runtime)
            //implementation(libs.androidx.paging.compose)

            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation("com.attafitamim.krop:ui:0.1.5")
            implementation("com.attafitamim.krop:core:0.1.5")

            implementation("network.chaintech:compose-multiplatform-media-player:1.0.38")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

            implementation(project.dependencies.platform("org.kotlincrypto.hash:bom:0.7.0"))
            implementation("org.kotlincrypto.hash:sha2")


        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.lovorise.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.lovoriseapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 20
        versionName = "1.3"
    }

    signingConfigs {
        create("release") {
            storeFile = file("/Users/akash/Documents/LovoriseKeystore/LovoriseKey")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug"){
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
        //release
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
          //  isShrinkResources = true
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
    buildFeatures {
        compose = true
    }
    dependencies {
        ksp(libs.androidx.room.compiler)
        implementation (libs.androidx.core.splashscreen)
        debugImplementation(compose.uiTooling)
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}

dependencies {
//    ksp(libs.androidx.room.compiler)
//    ksp(project.dependencies.platform(libs.koin.annotation.bom))
   // ksp(libs.koin.annotation.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
}


