cmake_minimum_required(VERSION 3.18.1)
project("serialport")

add_library(
        serialport
        SHARED
        src/main/cpp/SerialPort.c
)

find_library(log-lib log)

target_link_libraries(
        serialport
        ${log-lib}
)