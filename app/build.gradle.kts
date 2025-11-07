/*
Copyright 2025 sby1ce

SPDX-License-Identifier: CC0-1.0
*/

import com.android.build.gradle.tasks.MergeSourceSetFolders
import com.nishtahir.CargoBuildTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.mozilla.rust) apply true
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

cargo {
    module = "../countdown-rs"
    libname = "cd_android"
    verbose = true
    targets = listOf("arm64", "x86_64")
    prebuiltToolchains = true
    profile = "release"
    apiLevel = 24
}

val task = tasks.register<Exec>("uniffiBindgen") {
    workingDir = file("${project.rootDir}/countdown-rs/cd-android")
    val libs = "${project.rootDir}/app/build/rustJniLibs/android/" +
            "arm64-v8a/libcd_android.so"
    commandLine(
        "cargo",
        "run",
        "--bin",
        "uniffi-bindgen",
        "generate",
        "--library",
        libs,
        "--language",
        "kotlin",
        "--out-dir",
        layout.buildDirectory.dir("generated/kotlin").get().asFile.path
    )
}

project.afterEvaluate {
    tasks.withType(CargoBuildTask::class).forEach { buildTask ->
        tasks.withType(MergeSourceSetFolders::class).configureEach {
            this.inputs.dir(
                layout.buildDirectory.dir(
                    "rustJniLibs" +
                            File.separatorChar + buildTask.toolchain!!.folder
                )
            )
            this.dependsOn(buildTask)
        }
    }
}

tasks.preBuild.configure {
    dependsOn.add(tasks.withType(CargoBuildTask::class.java))
    dependsOn.add(task)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget("1.8")
    }
}

android {
    namespace = "com.example.countdowntimers"
    compileSdk = 36
    ndkVersion = "29.0.14033849"
    sourceSets {
        getByName("main").java.srcDir("build/generated/kotlin")
    }

    defaultConfig {
        applicationId = "com.example.countdowntimers"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += "META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.jna) {
        artifact {
            extension = "aar"
            type = "aar"
        }
    }
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
