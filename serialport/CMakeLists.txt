cmake_minimum_required(VERSION 3.18.1)
project("serialport")

add_library(
        serialport
        SHARED
        src/main/cpp/SerialPort.c
)

find_library(log-lib log)

target_link_options(serialport PRIVATE "-Wl,-z,max-page-size=16384")

target_link_libraries(
        serialport
        ${log-lib}
)