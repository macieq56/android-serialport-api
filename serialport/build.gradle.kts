plugins {
  alias(libs.plugins.android.library)
  `maven-publish`
}

android {
  namespace = "pl.macieq56.serialport"
  compileSdk {
    version = release(36)
  }

  publishing {
    singleVariant("release") {
      withSourcesJar()
    }
  }

  defaultConfig {
    minSdk = 25

    externalNativeBuild {
      cmake {
        cppFlags += "-std=c++17"
      }
    }
    ndk {
      abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
    }

    testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }
  externalNativeBuild {
    cmake {
      path = file("CMakeLists.txt")
    }
  }

  sourceSets {
    getByName("main").jniLibs.directories.add("src/main/jniLibs")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin {
  compilerOptions {
    jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
  }
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("release") {
        from(components["release"])
        groupId = project.findProperty("group")?.toString() ?: "pl.macieq56"
        artifactId = "serialport"
        version = project.findProperty("version")?.toString() ?: "1.0.4"
      }
    }
  }
}
