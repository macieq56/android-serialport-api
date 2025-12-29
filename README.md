# Android Serial Port Library (Kotlin + Native)

A lightweight Android library for accessing Linux serial ports using platform-native `/dev/tty*` device files.  
Written in Kotlin with JNI bindings and a native C implementation.

This project is based on the Android Serial Port API by Cedric Priscal (Apache License 2.0), updated for modern Android and Kotlin.

---

## Features

- Kotlin API for opening and closing serial ports
- Enumerates available serial devices under `/dev/`
- Native binding via `.so` library
- Packaged as a standard Android `.aar`
- Compatible with multiple ABIs (depending on Gradle configuration)

---

## Installation

### Option A — Dependency via JitPack (recommended)

1. Add JitPack to repositories in `settings.gradle.kts` (or `settings.gradle`):

   ```
   dependencyResolutionManagement {
       repositories {
           google()
           mavenCentral()
           maven { url = uri("https://jitpack.io") }
        }
   }
   ```

2. Add dependency in your app module `build.gradle.kts`:

```
   dependencies {
      implementation("com.github.macieq56:android-serialport-api:v1.0.0")
   }
```

---

### Option B — Include module directly

If this library module is in the same Gradle project as your app:

1. In `settings.gradle.kts`:
```
   include(":serialport")
```
2. In your app module `build.gradle.kts`:
```
   dependencies {
      implementation(project(":serialport"))
   }
```
---

## Build from source

To build the release AAR (including native `.so` libraries):
```
    ./gradlew :serialport:assembleRelease
```
Artifacts:

- AAR: `serialport/build/outputs/aar/serialport-release.aar`
- Native `.so`: included inside the AAR for configured ABIs

To control which ABIs are built, edit `serialport/build.gradle.kts`:
```
    android {
        defaultConfig {
            ndk {
                // Example: only 32-bit ARM
                abiFilters.clear()
                abiFilters += listOf("armeabi-v7a")
            }
        }
    }
```
---

## Usage
```
import pl.macieq56.serialport.SerialPort
import java.io.File

    try {
        val port = SerialPort(
        device = File("/dev/ttyS1"),
        baudrate = 9600,
        flags = 0
    )
    
        val input = port.inputStream
        val output = port.outputStream
    
        // Example: send data
        output?.write("Hello\n".toByteArray())
    
        // Example: receive data
        val buffer = ByteArray(64)
        val bytesRead = input?.read(buffer)
        if (bytesRead != null && bytesRead > 0) {
            val response = String(buffer, 0, bytesRead)
            println("Received: $response")
        }
    
        port.close()
    
    } catch (e: Exception) {
        e.printStackTrace()
    }

```
---

## Enumerating serial devices

Use `SerialPortFinder` to discover available serial devices:
```
    import com.example.serialport.SerialPortFinder

    val finder = SerialPortFinder()

    // Human-readable device names, e.g. "ttyS1 (serial)"
    val deviceLabels: Array<String> = finder.getAllDevices()

    // Absolute paths, e.g. "/dev/ttyS1"
    val devicePaths: Array<String> = finder.getAllDevicesPath()
```
`SerialPortFinder` parses `/proc/tty/drivers` and `/dev` to identify serial-capable devices.

---

## Permissions & Notes

- Access to `/dev/tty*` devices depends on device/vendor permissions.  
  On some systems you may need additional permissions or root, for example:

  chmod 666 /dev/ttyS*

- This library is low-level and assumes the user understands:
    - how character devices work in Linux,
    - how file descriptors behave,
    - Android security and permission model.

---

## License & Credits

This project is licensed under the **Apache License, Version 2.0**.

See:
- `LICENSE`
- `NOTICE`

This library includes modified code from:

- Android Serial Port API — Cedric Priscal (Apache 2.0)

Kotlin port, project structure and adaptations:

- Maciej Krokosz, 2025
