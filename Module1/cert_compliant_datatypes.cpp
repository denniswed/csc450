// SEI CERT C++ Compliant Data Types Demonstration
// This program demonstrates various C++ data types while following
// SEI CERT C++ coding standards

#include <iostream>
#include <stdexcept>
#include <string>

// Function to safely display personal information
// Following FIO50-CPP: Do not alternately input and output from a stream
void displayPersonalInfo() {
  try {
    // Different data types demonstration with explicit initialization
    const std::string name{"John Doe"};        // string type (const for safety)
    const std::string birthdate{"1990-05-15"}; // string type for date
    const int age{33};          // integer type (brace initialization)
    const double height{5.9};   // double type for decimal
    const char grade{'A'};      // character type
    const bool isStudent{true}; // boolean type

    // Additional data types for demonstration
    const unsigned int studentId{12345U}; // unsigned integer
    const long long ssn{123456789LL};     // long long type
    const float gpa{3.85F};               // float type (explicit F suffix)
    const short creditHours{15};          // short type

    // Display the results with error checking
    // Following ERR50-CPP recommendations for I/O operations
    std::cout << "=== Personal Information ===" << std::endl;

    if (!std::cout) {
      throw std::runtime_error("Output stream error");
    }

    std::cout << "Name: " << name << std::endl;
    std::cout << "Birthdate: " << birthdate << std::endl;
    std::cout << "Age: " << age << " years old" << std::endl;
    std::cout << "Height: " << height << " feet" << std::endl;
    std::cout << "Grade: " << grade << std::endl;
    std::cout << "Is Student: " << (isStudent ? "Yes" : "No") << std::endl;
    std::cout << "Student ID: " << studentId << std::endl;
    std::cout << "SSN: " << ssn << std::endl;
    std::cout << "GPA: " << gpa << std::endl;
    std::cout << "Credit Hours: " << creditHours << std::endl;

    // Demonstrate type sizes (platform-dependent information)
    std::cout << "\n=== Data Type Sizes ===" << std::endl;
    std::cout << "Size of int: " << sizeof(int) << " bytes" << std::endl;
    std::cout << "Size of double: " << sizeof(double) << " bytes" << std::endl;
    std::cout << "Size of char: " << sizeof(char) << " bytes" << std::endl;
    std::cout << "Size of bool: " << sizeof(bool) << " bytes" << std::endl;
    std::cout << "Size of string: " << sizeof(std::string) << " bytes"
              << std::endl;

  } catch (const std::exception &e) {
    // ERR50-CPP: Handle exceptions properly instead of abrupt termination
    std::cerr << "Error: " << e.what() << std::endl;
    throw; // Re-throw to let main handle it
  }
}

int main() noexcept {
  try {
    displayPersonalInfo();
    return 0; // Explicit return value

  } catch (const std::exception &e) {
    // ERR50-CPP: Proper error handling
    std::cerr << "Program terminated due to error: " << e.what() << std::endl;
    return 1; // Return non-zero for error

  } catch (...) {
    // Catch any other exceptions
    std::cerr << "Program terminated due to unknown error" << std::endl;
    return 1;
  }
}
