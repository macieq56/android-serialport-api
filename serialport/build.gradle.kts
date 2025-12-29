plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "pl.macieq56.serialport"
  compileSdk {
    version = release(36)
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
    getByName("main").jniLibs.srcDirs("src/main/jniLibs")
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
  kotlinOptions {
    jvmTarget = "17"
  }
}
