/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Converted to Kotlin by Maciej Krokosz, 2025
 */

package pl.macieq56.serialport

import android.util.Log
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.LineNumberReader

class SerialPortFinder {

  inner class Driver(
    private val driverName: String,
    private val deviceRoot: String
  ) {
    private var devices: MutableList<File>? = null

    fun getDevices(): List<File> {
      if (devices == null) {
        devices = mutableListOf()
        val devDir = File("/dev")
        val files = devDir.listFiles()?.toList().orEmpty()
        for (file in files) {
          if (file.absolutePath.startsWith(deviceRoot)) {
            Log.d(TAG, "Found new device: $file")
            devices?.add(file)
          }
        }
      }
      return devices.orEmpty()
    }

    fun getName(): String = driverName
  }

  private var drivers: MutableList<Driver>? = null

  @Throws(IOException::class)
  private fun getDrivers(): List<Driver> {
    if (drivers == null) {
      drivers = mutableListOf()
      LineNumberReader(FileReader("/proc/tty/drivers")).use { reader ->
        var line: String?
        while (reader.readLine().also { line = it } != null) {
          val l = line ?: continue

          // Driver name may contain spaces → use substring
          val driverName = l.substring(0, 0x15).trim()
          val parts = l.split(Regex(" +"))

          if (parts.size >= 5 && parts.last() == "serial") {
            val root = parts[parts.size - 4]
            Log.d(TAG, "Found new driver $driverName on $root")
            drivers?.add(Driver(driverName, root))
          }
        }
      }
    }
    return drivers.orEmpty()
  }

  fun getAllDevices(): Array<String> = try {
    val devices = mutableListOf<String>()
    for (driver in getDrivers()) {
      for (file in driver.getDevices()) {
        devices.add("${file.name} (${driver.getName()})")
      }
    }
    devices.toTypedArray()
  } catch (e: IOException) {
    e.printStackTrace()
    emptyArray()
  }

  fun getAllDevicesPath(): Array<String> = try {
    val devices = mutableListOf<String>()
    for (driver in getDrivers()) {
      for (file in driver.getDevices()) {
        devices.add(file.absolutePath)
      }
    }
    devices.toTypedArray()
  } catch (e: IOException) {
    e.printStackTrace()
    emptyArray()
  }

  companion object {
    private const val TAG = "SerialPort"
  }
}
