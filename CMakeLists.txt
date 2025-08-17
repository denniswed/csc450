# CMake minimum version required
cmake_minimum_required(VERSION 3.16)

# Project name and version
project(CSC450_Examples VERSION 1.0)

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add compiler flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra")
set(CMAKE_CXX_FLAGS_DEBUG "-g -O0")
set(CMAKE_CXX_FLAGS_RELEASE "-O2 -DNDEBUG")

# Create executable for hello world
add_executable(hello_world "Module1/hello world.cpp")

# Create executable for debug example
add_executable(debug_example "Module1/debug_example.cpp")

# Optional: Set output directory
set_target_properties(hello_world debug_example PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/bin"
)
